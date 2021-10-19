package com.example.hiwin.teacher_version_bob.data.framework.face.parser;

import com.example.hiwin.teacher_version_bob.data.framework.face.DataFace;

public interface DataFaceParser<DATA> {
    DataFace parse(DATA data) throws Exception;
}
