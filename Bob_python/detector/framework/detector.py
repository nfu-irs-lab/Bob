import abc
import threading
from abc import ABC
from typing import Optional


class DetectListener(ABC):
    @abc.abstractmethod
    def onDetect(self, data):
        pass


class Detector(threading.Thread, ABC):

    def __init__(self, listener: DetectListener):
        super().__init__()
        self._listener: Optional[DetectListener] = listener
        self.__interrupt = False
        self.__start = False

    @abc.abstractmethod
    def _detect(self):
        pass

    def run(self) -> None:
        try:
            self._detect()
        except Exception as e:
            print(e.__str__())
            # self.stop()

    def _interrupted(self) -> bool:
        return self.__interrupt

    def _running(self) -> bool:
        return self.__start

    def resume(self):
        self.__start = True

    def pause(self):
        self.__start = False

    def stop(self):
        self.__interrupt = True
