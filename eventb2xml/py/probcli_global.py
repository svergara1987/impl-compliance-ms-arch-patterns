import logging

PROGRAM_NAME = 'probcli_test_generator.py'
VERBOSE_MODE = False


def set_up_logger(verbose):
    logger = logging.getLogger(PROGRAM_NAME)
    std_output_handler = logging.StreamHandler()
    if verbose:
        logger.setLevel(logging.DEBUG)
        std_output_handler.setLevel(logging.DEBUG)
    else:
        logger.setLevel(logging.INFO)
        std_output_handler.setLevel(logging.INFO)
    log_formatter = logging.Formatter('%(asctime)s - %(message)s')
    # log_formatter = logging.Formatter('\n%(asctime)s - %(name)s - %(levelname)s - %(message)s\n')
    std_output_handler.setFormatter(log_formatter)
    logger.addHandler(std_output_handler)
    # end setting up logger


def get_logger():
    return logging.getLogger(PROGRAM_NAME)
