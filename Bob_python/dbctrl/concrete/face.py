import json
from typing import List, TextIO

from dbctrl.framework.data import Data, DataParser


class Language:
    def __init__(self, code: str, tr_name: str, tr_sentence: str):
        self.tr_sentence = tr_sentence
        self.tr_name = tr_name
        self.code = code


class Face(Data):
    def __init__(self, name: str, sentence: str, languages: List[Language]):
        self.languages = languages
        self.sentence = sentence
        self.name = name

    def getSpecifyLanguage(self, code: str) -> Language:
        for language in self.languages:
            if language.code is code:
                return language

        raise Exception()

    def getQueryId(self):
        return self.name


class JSONFaceParser(DataParser):

    def __init__(self):
        super().__init__()

    def parse(self, content: TextIO) -> List[Face]:
        objectList: List[Face] = []
        json_array = json.load(content)

        for json_object in json_array:
            languages: List[Language] = []
            json_languages_array = json_object['languages']
            for lan in json_languages_array:
                languages.append(Language(lan['code'], lan['tr_name'], lan['tr_sentence']))

            objectList.append(
                Face(json_object['name'], json_object['sentence'],
                     languages))
        return objectList
