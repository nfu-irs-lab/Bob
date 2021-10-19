import os
from typing import List, Optional

from dbctrl.concrete.database import FileDatabase
# from dbctrl.concrete.object import JSONObjectParser, Object
from dbctrl.concrete.face import JSONFaceParser, Face
from dbctrl.concrete.object import Object, JSONObjectParser
from dbctrl.framework.data import Data

db_charset = 'UTF-8'
db_location = f"db{os.path.sep}faces.json"
db = FileDatabase(open(db_location, encoding=db_charset), JSONFaceParser())
face: Optional[Face] = db.queryForId("happy")
print(face.sentence)

db_location = f"db{os.path.sep}objects.json"
db = FileDatabase(open(db_location, encoding=db_charset), JSONObjectParser())
obj: Optional[Object] = db.queryForId("car")
print(obj.sentence)
