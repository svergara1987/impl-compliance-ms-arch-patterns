#!/usr/bin/env python3

import os
import yaml
import subprocess
import probcli_global


def execute_strategy(machine_file, sequence_strategy, output_file):
    probcli_global.get_logger().debug('starting to execute sequence strategy')
    output_dir = output_file
    temp_dir = '{}/sequence'.format(output_dir)
    os.makedirs(temp_dir)
    strategy_name = sequence_strategy['metadata']['name'].replace(' ', '_')
    temp_strategy_file_path = '{}/{}.yaml'.format(temp_dir, strategy_name)
    temp_strategy_file = open(temp_strategy_file_path, 'w')
    yaml.dump(sequence_strategy, temp_strategy_file)
    probcli_global.get_logger().debug('saved temporal strategy file to {}'.format(temp_strategy_file_path))
    (head, tail) = os.path.split(os.path.abspath(__file__))
    # process = subprocess.Popen(['gradle', 'run', '--args="--machine \'{}\' --strategy \'{}\'"'.format(
    #     machine_file, temp_strategy_file_path),'-p', os.path.join(head, '../java')], stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    java_generated_file_path = output_dir + '/' + strategy_name + '.xml'
    args_param = '--args="--machine \'' + machine_file + '\' --strategy \'' + temp_strategy_file_path + '\' --output \'' + java_generated_file_path + '\'"'
    probcli_global.get_logger().debug('args parameter for gradle {}'.format(args_param))
    p_param = ' -p \'' + os.path.join(head, '../java') + '\''
    probcli_global.get_logger().debug('-p parameter for gradle {}'.format(p_param))
    gradle_command = 'gradle run ' + args_param + p_param
    probcli_global.get_logger().debug('gradle_command {}'.format(gradle_command))
    stream = os.popen(gradle_command)
    # check for errors in execution
    for line in stream.readlines():
        probcli_global.get_logger().debug('gradle_output: {}'.format(line))
        if 'BUILD FAILED' in line:
            raise Exception(tail)
    probcli_global.get_logger().debug('ended executing sequence strategy')
