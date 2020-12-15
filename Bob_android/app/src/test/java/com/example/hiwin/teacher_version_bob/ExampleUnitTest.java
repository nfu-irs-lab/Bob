package com.example.hiwin.teacher_version_bob;

import com.example.hiwin.teacher_version_bob.protocol.ClientHelloPackage;
import com.example.hiwin.teacher_version_bob.protocol.ServerHelloPackage;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void numberConversionTest(){
        byte raw=127;
        int length = 0b11111111&raw;
    }

    void dumpBytes(byte[] raw){
        System.out.print("{");
        for(byte b:raw){
            System.out.print(b+",");
        }
        System.out.println("}");
    }

}