package com.example.hiwin.teacher_version_bob.protocol;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class MessagePackage extends Package {
	
	public MessagePackage(byte[] importBytes) {
		super(importBytes);
	}

	public MessagePackage(String msg) {
		super((byte) 0x01, setData(msg));
	}
	
	public String getMessage() {
		byte[] raw_data=android.util.Base64.decode(getData(), android.util.Base64.DEFAULT);
		return new String(raw_data, StandardCharsets.UTF_8);
	}
	
	private static byte[] setData(String msg) {
		String str=android.util.Base64.encodeToString(msg.getBytes(StandardCharsets.UTF_8), android.util.Base64.DEFAULT);
//			System.out.println(str);
		return str.getBytes(StandardCharsets.UTF_8);
	}
}
