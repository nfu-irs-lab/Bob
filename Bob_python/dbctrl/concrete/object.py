import json
from typing import TextIO, List

from dbctrl.framework.object import ObjectParser, Object, Language


class JSONObjectParser(ObjectParser):

    def __init__(self):
        super().__init__()

    def parse(self, content: TextIO) -> List[Object]:
        objectList: List[Object] = []
        json_array = json.load(content)

        for json_object in json_array:
            languages: List[Language] = []
            json_languages_array = json_object['languages']
            for lan in json_languages_array:
                languages.append(Language(lan['code'], lan['tr_name'], lan['tr_sentence']))

            objectList.append(
                Object(json_object['name'], json_object['group'], json_object['sentence'], json_object['action'],
                       languages))
        return objectList
