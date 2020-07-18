#!/usr/bin/env python3

import subprocess
import probcli_global


def run_probcli_cbc(machine_file, depth, end_predicate, output_file, timout_in_seconds):
    # probcli m0_circuit_breaker_mch.eventb -cbc_tests 5 circuit_breaker=HALF_OPEN test_half_open.xml -p CLPFD true -p TIME_OUT 60000
    probcli_global.get_logger().debug('starting run_probcli_cbc with parameters machine_file=\'{}\' depth=\'{}\' end_predicate=\'{}\' output_file=\'{}\' timout_in_seconds=\'{}\''.format(machine_file, depth, end_predicate, output_file, timout_in_seconds))
    process = subprocess.Popen(['probcli', machine_file, '-cbc_tests', str(depth), end_predicate, output_file, '-p',
                                'CLPFD', 'true', '-p', 'TIME_OUT', str(timout_in_seconds*1000)], stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    stdout, stderr = process.communicate()
    if stdout:
        for line in stdout.decode('utf-8').split(sep='\n'):
            probcli_global.get_logger().debug('probcli_cbc stdout: {}'.format(line))
    if stderr:
        for line in stderr.decode('utf-8').split(sep='\n'):
            probcli_global.get_logger().debug('probcli_cbc stderr: {}'.format(line))
    probcli_global.get_logger().debug('ended executing run_probcli_cbc')
    

def execute_strategy(machine_file, cbc_strategy, output_file):
    probcli_global.get_logger().debug('starting to execute cbc strategy')
    output_dir = output_file
    strategy_id = 0
    for an_expression in cbc_strategy['expression']:
        strategy_id += 1
        strategy_name = cbc_strategy['metadata']['name'].replace(' ', '_')
        run_probcli_cbc(machine_file=machine_file, depth=cbc_strategy['steps'], end_predicate=an_expression, output_file='{}/{}_{}.xml'.format(
                        output_dir, strategy_name, strategy_id), timout_in_seconds=cbc_strategy['timeout'])
    probcli_global.get_logger().debug('ended executing cbc strategy')
