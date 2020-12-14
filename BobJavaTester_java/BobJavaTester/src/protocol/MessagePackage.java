package protocol;

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
		byte[] data = getData();
		Decoder decoder = Base64.getDecoder();
		byte[] raw_data = decoder.decode(data);
		String raw_message = null;
//		try {
		raw_message = new String(raw_data, StandardCharsets.UTF_8);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return raw_message;
	}

	private static byte[] setData(String msg) {
		Encoder encoder = Base64.getEncoder();
		String str = null;
//		try {
		str = encoder.encodeToString(msg.getBytes(StandardCharsets.UTF_8));
//			System.out.println(str);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		try {
		return str.getBytes(StandardCharsets.UTF_8);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return null;
//		}
	}
}
