package com.example.hiwin.teacher_version_bob;

import com.example.hiwin.teacher_version_bob.protocol.DataPackage;
import com.example.hiwin.teacher_version_bob.protocol.SplitDataPackage;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void numberConversionTest(){
        byte raw= (byte) (0b11111111&-2);
        int length = 0b11111111&raw;
        System.out.println(length);
    }
    @Test
    public void splitTest(){
        DataPackage dataPackage=new DataPackage();
        byte[] raw={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        ArrayList<SplitDataPackage> datas=DataPackage.splitPackage(raw);
        dataPackage.addAll(datas);
        dumpBytesInHex(dataPackage.getData());
        System.out.println();
    }
    @Test
    public void splitBytesTest(){
        byte[] raw={1,2,3,4,5,6,7,8,9,10,11,12};
        dumpBytesInHex(raw);
        dumpBytesInHex(splitBytes(raw,0,5));
        dumpBytesInHex(splitBytes(raw,5,3));

    }
    private byte[] splitBytes(byte[] raw,int start,int length){
        byte[] splited=new byte[length];
        for(int i=0;i<length;i++){
            splited[i]=raw[start+i];
        }
        return splited;
    }


    void dumpBytesInHex(byte[] raw){
        System.out.print("{");
        for(byte b:raw){
            System.out.print("0x"+Integer.toHexString(b)+",");
        }
        System.out.println("}");
    }
    String BytesInHexString(byte[] raw){
        StringBuffer sb=new StringBuffer();

        sb.append("{");
        for(byte b:raw){
            sb.append("0x").append(Integer.toHexString(b)).append(",");
        }
        sb.append("}");
        return sb.toString();
    }

}