from typing import Optional, TextIO

from dbctrl.framework.data import Data, DataParser
from dbctrl.framework.database import Database


class FileDatabase(Database):

    def __init__(self, file: TextIO, parser: DataParser):
        self.file = file
        self.object_array = parser.parse(file)
        super().__init__()

    def queryForId(self, name: str) -> Optional[Data]:
        for obj in self.object_array:
            if name == obj.getQueryId():
                return obj

        return None
