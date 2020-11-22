package com.example.hiwin.teacher_version_bob;

import com.example.hiwin.teacher_version_bob.protocal.EnablePatternRecognitionPackage;
import com.example.hiwin.teacher_version_bob.protocal.Package;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void PackageTest() {
//        byte[] import_bytes=new byte[]{(byte)0xff,(byte)0xef,(byte)0x01,(byte)0x00,(byte)0xfe};
//        Package p=new Package(import_bytes);
        EnablePatternRecognitionPackage eprp=new EnablePatternRecognitionPackage();
        eprp.setEnabled(true);
        byte o[]=eprp.toBytes();
        if(true)o.toString();
    }
}