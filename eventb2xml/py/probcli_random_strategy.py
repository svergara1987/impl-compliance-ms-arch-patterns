import os
import subprocess
import re
import copy
import sys
from lxml import etree  # https://lxml.de/tutorial.html
import probcli_global


def run_probcli_mcm(machine_file, depth, max_states, end_predicate, output_file, operations_to_cover, timout_in_seconds):
    # probcli my.mch -mcm_tests 10 2000 "EndStateVar=TRUE" testcases.xml -mcm_cover op1,op2
    probcli_global.get_logger().debug('starting run_probcli_mcm with parameters machine_file=\'{}\' depth=\'{}\' max_states=\'{}\' end_predicate=\'{}\' output_file=\'{}\' operations_to_cover=\'{}\' timout_in_seconds=\'{}\''.format(machine_file, depth, max_states, end_predicate, output_file, operations_to_cover, timout_in_seconds))
    command = ['probcli', machine_file, '-mcm_tests', str(depth), str(max_states), end_predicate, output_file, '-p', 'TIME_OUT', 
        str(timout_in_seconds*1000)]
    if operations_to_cover and len(operations_to_cover) > 0:
        command.append('-mcm_cover')
        command.append(operations_to_cover[0])
        for i in range(1, len(operations_to_cover)):
            command.append(',{}'.format(operations_to_cover[i]))
    process = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    stdout, stderr = process.communicate()
    if stdout:
        for line in stdout.decode('utf-8').split(sep='\n'):
            probcli_global.get_logger().debug('probcli_mcm stdout: {}'.format(line))
    if stderr:
        for line in stderr.decode('utf-8').split(sep='\n'):
            probcli_global.get_logger().debug('probcli_mcm stderr: {}'.format(line))
    probcli_global.get_logger().debug('ended executing run_probcli_mcm')


def run_probcli_animate(machine_file, steps, output_file):
    # probcli -animate 100 -his animation_history.txt -his_option show_init -his_option show_states py/m0_circuit_breaker_mch.eventb
    probcli_global.get_logger().debug('starting run_probcli_animate with parameters machine_file=\'{}\' steps=\'{}\' output_file=\'{}\''.format(
                                      machine_file, steps, output_file))
    process = subprocess.Popen(['probcli', '-animate', str(steps), '-his', output_file, '-his_option',
                                'show_init', '-his_option', 'show_states', machine_file], stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    stdout, stderr = process.communicate()
    if stdout:
        for line in stdout.decode('utf-8').split(sep='\n'):
            probcli_global.get_logger().debug('probcli_animate stdout: {}'.format(line))
    if stderr:
        for line in stderr.decode('utf-8').split(sep='\n'):
            probcli_global.get_logger().debug('probcli_animate stderr: {}'.format(line))
    probcli_global.get_logger().debug('ended executing run_probcli_animate')


def execute_strategy(machine_file, random_strategy, output_file):
    probcli_global.get_logger().debug('starting to execute random strategy')
    # Hay que ejecutar:
    # probcli -animate 100 -his animation_history.txt -his_option show_init -his_option show_states py/m0_circuit_breaker_mch.eventb
    # para tener la historia, y luego
    # probcli my.mch -mcm_tests 10 2000 "EndStateVar=TRUE" testcases.xml -mcm_cover op1,op2
    # cubriendo todas las operaciones que hay en el xml para obtener los nombres de los parámetros de las operaciones
    output_dir = output_file
    count = random_strategy['count']
    if count is None:
        count = 1
    for strategy_id in range(1, count+1):
        probcli_global.get_logger().debug('generating random test case {}/{}'.format(strategy_id, count))
        strategy_name = random_strategy['metadata']['name'].replace(' ', '_')
        if not os.path.isdir('{}/random'.format(output_dir)):
            os.makedirs('{}/random'.format(output_dir))
            probcli_global.get_logger().debug('created temporal directory {}/random'.format(output_dir))
        animation_file_name = '{}/random/animation_{}.txt'.format(output_dir, strategy_id)
        run_probcli_animate(machine_file=machine_file, steps=random_strategy['steps']+2, output_file=animation_file_name)
        # starting xml genration
        probcli_global.get_logger().debug('starting random test case xml generation')
        new_extended_test_suite = etree.Element('extended_test_suite')
        new_extended_test_suite.text = '\n\t'
        a_test_case = etree.SubElement(new_extended_test_suite, "test_case")
        initialization = etree.SubElement(a_test_case, "initialisation")
        operations_found = {}
        current_scope = None
        current_operation = None
        machine_variables = {}        
        the_file = open(animation_file_name, 'r')
        probcli_global.get_logger().debug('starting to parse file {}'.format(animation_file_name))
        for line in the_file.readlines():
            probcli_global.get_logger().debug('line read: {}'.format(line.strip('\n')))
            if line.startswith('/* Constants'):
                current_scope = 1  # constants
                current_operation = initialization
                continue
            elif line.startswith('*/'):
                current_scope = None
                continue
            elif line.startswith('/* Initialisation'):
                current_operation = initialization
                continue
            elif line.startswith('/* Variables'):
                current_scope = 2  # variables
                continue
            if current_scope == 1:
                value = etree.SubElement(initialization, "value")
                value.attrib['type'] = 'constant'
                match = re.search(' *([a-z,A-Z].*) = (.*)', line)
                value.attrib['name'] = match.group(1)
                value.text = match.group(2)
            elif current_scope == 2:
                match = re.search(' *([a-z,A-Z].*) = (.*)', line)
                variable_name = match.group(1)
                variable_value = match.group(2)
                if variable_name in machine_variables.keys():
                    if machine_variables[variable_name] != variable_value:
                        modified = etree.SubElement(current_operation, "modified")
                        modified.attrib['name'] = variable_name
                        modified.text = variable_value
                        machine_variables[variable_name] = variable_value
                else:
                    value = etree.SubElement(current_operation, "value")
                    value.attrib['type'] = 'variable'
                    value.attrib['name'] = variable_name
                    value.text = variable_value
                    machine_variables[variable_name] = variable_value
            else:  # it's an operation
                # request(TRUE,OPEN,0,3,35);
                match = re.search('(^[a-z,A-Z].*)\((.*)\)', line)
                operation_name = match.group(1) #operation_name='request'
                operation_parameters = match.group(2).split(',') #operation_parameters=['TRUE', 'CLOSED', '0', '3', '0']
                step = etree.SubElement(a_test_case, "step")
                step.attrib['name'] = operation_name
                # to obtain operation parameters' names
                if operation_name not in operations_found.keys():
                    output_file_mcm = '{}/random/mcm_{}.xml'.format(output_dir, operation_name)
                    run_probcli_mcm(machine_file=machine_file, depth=sys.maxsize, max_states=sys.maxsize, end_predicate='1=1',
                                    output_file=output_file_mcm, operations_to_cover=[operation_name], timout_in_seconds=random_strategy['timeout'])
                    operation_parsed_xml = etree.parse(output_file_mcm)
                    operations_found[operation_name] = operation_parsed_xml.find('.//step[@name=\'{}\']'.format(operation_name))
                the_operation_step = copy.deepcopy(operations_found[operation_name])
                # logger.debug('>>> FOUND:')
                # logger.debug(etree.tostring(the_operation_step, pretty_print=True))
                the_operation_step_values = the_operation_step.findall('value')
                for i in range(0,len(the_operation_step_values)):
                    the_operation_step_values[i].text = operation_parameters[i]
                    step.append(the_operation_step_values[i])
                    probcli_global.get_logger().debug('appended step {} to operation {}'.format(etree.tostring(
                                  the_operation_step_values[i], pretty_print=True).decode("utf-8").replace('\n',''), operation_name))
                current_operation = step
        #saving xml
        probcli_global.get_logger().debug('test case xml to save: {}'.format(etree.tostring(new_extended_test_suite, pretty_print=True).decode("utf-8").replace('\n', '')))
        tree = etree.ElementTree(new_extended_test_suite)
        output_file_name = '{}/{}_{}.xml'.format(output_dir, strategy_name, strategy_id)
        tree.write(output_file_name, xml_declaration=True, pretty_print=True, encoding='utf-8')
    probcli_global.get_logger().debug('ended executing random strategy')