import json
from typing import List, Optional, TextIO

from dbctrl.concrete.object import JSONObjectParser
from dbctrl.framework.database import ObjectDatabase
from dbctrl.framework.object import Object


class JSONDatabase(ObjectDatabase):
    def __init__(self, file: TextIO):
        self.file = file
        self.object_array = JSONObjectParser().parse(file)
        super().__init__()

    def queryForName(self, name: str) -> Optional[Object]:

        for obj in self.object_array:
            if name == obj.name:
                return obj

        return None

    def queryForGroup(self, group: str) -> List[Object]:
        objectList: List[Object] = []

        for obj in self.object_array:
            if group == obj.group:
                objectList.append(obj)

        return objectList
