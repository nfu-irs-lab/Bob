import json
from typing import List, TextIO

from dbctrl.framework.data import Data, DataParser


class JSONData(Data):

    def __init__(self, queryId, data: json):
        self.data = data
        self.queryId = queryId

    def getData(self) -> json:
        return self.data

    def getQueryId(self):
        return self.queryId


class JSONDataParser(DataParser):

    def __init__(self):
        super().__init__()

    def parse(self, content: TextIO) -> List[JSONData]:
        objectList: List[JSONData] = []
        json_array: json = json.load(content)

        for json_object in json_array:
            objectList.append(JSONData(json_object['id'], json_object['data']))
        return objectList
