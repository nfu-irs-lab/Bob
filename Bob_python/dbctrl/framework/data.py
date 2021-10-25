import abc
from abc import ABC
from typing import List


class Data(ABC):
    @abc.abstractmethod
    def getQueryId(self):
        pass

    @abc.abstractmethod
    def getData(self):
        pass


class DataParser(ABC):
    @abc.abstractmethod
    def parse(self, content) -> List[Data]:
        pass
