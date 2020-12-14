package test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.junit.jupiter.api.Test;

import protocol.MessagePackage;

public class JUnitTester {
	@Test
	public void Base64Test() {
		MessagePackage messagePackage=new MessagePackage("Hello World 你好世界");
		MessagePackage messagePackage2=new MessagePackage(messagePackage.toBytes());
		System.out.println(messagePackage2.getMessage());
		
	}

}
