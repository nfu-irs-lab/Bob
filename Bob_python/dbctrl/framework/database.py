import abc
from typing import Optional
from dbctrl.framework.data import Data


class Database(abc.ABC):

    @abc.abstractmethod
    def queryForId(self, name: str) -> Optional[Data]:
        pass

    # @abc.abstractmethod
    # def queryForGroup(self, group: str) -> List[Data]:
    #     pass
