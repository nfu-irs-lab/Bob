import os
from typing import List

from Bob.robot.concrete.crt_command import CSVCommandFactory
from Bob.robot.framework.fw_command import Command


def getCommandsFromFileName(file: str) -> List[Command]:
    factory = CSVCommandFactory(f'actions{os.path.sep}{file}')
    return factory.createList()
