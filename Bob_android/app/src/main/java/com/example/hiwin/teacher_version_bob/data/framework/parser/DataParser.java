package com.example.hiwin.teacher_version_bob.data.framework.parser;

public interface DataParser<DATA,RETURN> {
    RETURN parse(DATA data) throws Exception;
}
