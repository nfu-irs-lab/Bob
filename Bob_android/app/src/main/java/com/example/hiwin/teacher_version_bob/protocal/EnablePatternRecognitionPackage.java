package com.example.hiwin.teacher_version_bob.protocal;

public class EnablePatternRecognitionPackage extends Package{
    public EnablePatternRecognitionPackage() {
        super((byte)1);
        data=new byte[1];
        length= (byte) data.length;
    }

    public void setEnabled(boolean functionEnabled) {
        setEnabled((byte)(functionEnabled?1:0));
    }
    public void setEnabled(byte enable) {
        data[0]=enable;
    }

    @Override
    public byte[] toBytes() {

        return super.toBytes();
    }
}
