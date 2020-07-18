#!/usr/bin/env python3

import os
import shutil
import argparse
import datetime
import yaml
import subprocess
import probcli_global
import probcli_cbc_strategy
import probcli_random_strategy
import probcli_sequence_strategy
import probcli_merger


def get_timestamp():
    return datetime.datetime.today().strftime('%Y%m%dT%H%M%S%f')[:-3]


def parse_test_strategy_file(test_strategy_yaml_file):
    to_return = []
    with open(test_strategy_yaml_file, 'r') as yaml_file:
        documents = yaml.load_all(yaml_file, Loader=yaml.FullLoader)
        probcli_global.get_logger().debug('{} parsed'.format(test_strategy_yaml_file))
        document_count = 0
        for a_document in documents:
            document_count += 1
            probcli_global.get_logger().debug('document[{}]={}'.format(document_count, a_document))
            to_return.append(a_document)
    return to_return


def merge_all_xml_within_directory(directory):
    probcli_global.get_logger().debug('starting to run merge_all_xml_within_directory directory=\'{}\''.format(directory))
    files_to_merge = []
    for file in os.listdir(directory):
        if file.endswith(".xml"):
            files_to_merge.append(os.path.join(directory, file))
    (head, tail) = os.path.split(directory)
    probcli_merger.join_files(list_input_files=files_to_merge, output_file_name='{}/{}'.format(directory, tail))
    # command_to_execute = ['python3', 'probcli_merger.py', '-i']
    # # list all .xml files within directory
    # for file in os.listdir(directory):
    #     if file.endswith(".xml"):
    #         command_to_execute.append(os.path.join(directory, file))
    # command_to_execute.append('-o')
    # (head, tail) = os.path.split(directory)
    # command_to_execute.append('{}/{}'.format(directory, tail))
    # command_to_execute.append('--verbose')
    # out = subprocess.Popen(command_to_execute, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    # stdout, stderr = out.communicate()
    # if stdout:
    #     for line in stdout.decode('utf-8').split(sep='\n'):
    #         probcli_global.get_logger().debug('probcli_merger.py stdout: {}'.format(line))
    # if stderr:
    #     for line in stderr.decode('utf-8').split(sep='\n'):
    #         probcli_global.get_logger().debug('probcli_merger.py stderr: {}'.format(line))
    probcli_global.get_logger().debug('ended executing merge_all_xml_within_directory')


def execute_test_strategy(machine_file, test_strategy, output_file, keep_files):
    probcli_global.get_logger().debug('creating temp work directory {}'.format(output_file))
    os.makedirs(output_file)
    probcli_global.get_logger().debug('getting absolute paths from relatives')
    probcli_global.get_logger().debug('output file \'{}\' -> \'{}\''.format(output_file, os.path.abspath(output_file)))
    output_file = os.path.abspath(output_file)
    for a_strategy in test_strategy:
        if a_strategy['kind'] == 'cbc':
            probcli_cbc_strategy.execute_strategy(machine_file=machine_file, cbc_strategy=a_strategy, output_file=output_file)
        elif a_strategy['kind'] == 'sequence':
            probcli_sequence_strategy.execute_strategy(machine_file=machine_file, sequence_strategy=a_strategy,     
                                                       output_file=output_file)
        elif a_strategy['kind'] == 'random':
            probcli_random_strategy.execute_strategy(machine_file=machine_file, random_strategy=a_strategy, output_file=output_file)
    merge_all_xml_within_directory(directory=output_file)
    probcli_global.get_logger().debug('moving result file to {}'.format(output_file))
    (head, tail) = os.path.split(output_file)
    probcli_global.get_logger().debug('renaming {} to {}'.format(output_file, os.path.join(head, 'delete_{}'.format(tail))))
    os.rename(output_file, os.path.join(head, 'delete_{}'.format(tail)))
    probcli_global.get_logger().debug('moving {} to {}'.format(
        os.path.join(head, 'delete_{}/{}'.format(tail, tail)), output_file))
    shutil.move(os.path.join(head, 'delete_{}/{}'.format(tail, tail)), output_file)
    if not keep_files:
        probcli_global.get_logger().debug('deleting temporal directory {}'.format(os.path.join(head, 'delete_{}'.format(tail))))
        shutil.rmtree(os.path.join(head, 'delete_{}'.format(tail)))
    probcli_global.get_logger().debug('test stratey executed')


def main():
    # start arguments handling
    # https://stackabuse.com/command-line-arguments-in-python/
    # https://docs.python.org/3/library/argparse.html#name-or-flags
    parser = argparse.ArgumentParser(prog=probcli_global.PROGRAM_NAME, epilog='Usage example: python3 {} -s strategy.yaml -m m0_circuit_breaker_mch.eventb -o test_cases.xml'.format(probcli_global.PROGRAM_NAME))
    parser.add_argument("-k", "--keep", help='prevents program from eliminating temporal files and directories', action='store_true')
    parser.add_argument("-m", "--machine", help='.eventb machine file')
    parser.add_argument("-o", "--output", help='name of the output file containing the test cases. Defaults to [timestamp]_test_cases.xml', default='{}_test_cases.xml'.format(get_timestamp()))
    parser.add_argument("-s", "--strategy", help='test strategy yaml file. Defaults to strategy.yaml', default='strategy.yaml')
    parser.add_argument('-v', '--verbose', help='enables verbose mode', action='store_true')
    args = parser.parse_args()
    # print(vars(args))
    # end arguments handling
    # start setting up logger
    probcli_global.set_up_logger(verbose=args.verbose) 
    probcli_global.get_logger().debug('program arguments={}'.format(args))
    probcli_global.get_logger().debug('getting absolute paths from relatives')
    probcli_global.get_logger().debug('machine file \'{}\' -> \'{}\''.format(args.machine, os.path.abspath(args.machine)))
    machine_file = os.path.abspath(args.machine)
    probcli_global.get_logger().debug('test strategy file \'{}\' -> \'{}\''.format(args.strategy,os.path.abspath(args.strategy)))
    test_strategy_file = os.path.abspath(args.strategy)
    execute_test_strategy(machine_file=machine_file, test_strategy=parse_test_strategy_file(test_strategy_file), 
                          output_file=args.output, keep_files=args.keep)
    probcli_global.get_logger().debug('test cases saved as {} following the {} strategy'.format(args.output, args.strategy))


if __name__ == '__main__':
    main()
