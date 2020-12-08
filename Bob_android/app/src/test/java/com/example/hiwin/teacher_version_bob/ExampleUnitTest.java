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
    public void ClientHelloPackageTest() {

        System.out.println("ClientHelloPackageTest");
        ClientHelloPackage s1=new ClientHelloPackage();
        dumpBytes(s1.toBytes());
        ClientHelloPackage clientHelloPackage=new ClientHelloPackage(s1.toBytes());
        dumpBytes(clientHelloPackage.toBytes());
    }

    @Test
    public void ServerHelloPackageTest() {
        System.out.println("ServerHelloPackageTest");
        ServerHelloPackage s1=new ServerHelloPackage(ServerHelloPackage.StatusCode.ALLOW);
        dumpBytes(s1.toBytes());
        ServerHelloPackage serverHelloPacakage=new ServerHelloPackage(s1.toBytes());
        dumpBytes(serverHelloPacakage.toBytes());
    }

    void dumpBytes(byte[] raw){
        System.out.print("{");
        for(byte b:raw){
            System.out.print(b+",");
        }
        System.out.println("}");
    }

}