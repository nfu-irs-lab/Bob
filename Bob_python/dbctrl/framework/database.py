import abc
from typing import Optional, List

from dbctrl.framework.object import Object


class ObjectDatabase(metaclass=abc.ABCMeta):

    def __init__(self):
        pass

    @abc.abstractmethod
    def queryForName(self, name: str) -> Optional[Object]:
        pass

    @abc.abstractmethod
    def queryForGroup(self, group: str) -> List[Object]:
        pass

