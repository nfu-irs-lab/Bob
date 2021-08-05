package com.example.hiwin.teacher_version_bob.data.framework.object.parser;

import com.example.hiwin.teacher_version_bob.data.framework.object.DataObject;

public interface DataObjectParser<DATA> {

    DataObject parse(DATA data) throws Exception;
}
