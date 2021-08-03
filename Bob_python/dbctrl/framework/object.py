import abc
from typing import List


class Language:
    def __init__(self, code: str, tr_name: str, tr_sentence: str):
        self.tr_sentence = tr_sentence
        self.tr_name = tr_name
        self.code = code


class Object:
    def __init__(self, name: str, group: str, sentence: str, action: str, languages: List[Language]):
        self.languages = languages
        self.action = action
        self.sentence = sentence
        self.group = group
        self.name = name

    def getSpecifyLanguage(self, code: str) -> Language:
        for language in self.languages:
            if language.code is code:
                return language

        raise Exception()


class ObjectParser(metaclass=abc.ABCMeta):

    def __init__(self):
        pass

    @abc.abstractmethod
    def parse(self, content) -> List[Object]:
        pass


