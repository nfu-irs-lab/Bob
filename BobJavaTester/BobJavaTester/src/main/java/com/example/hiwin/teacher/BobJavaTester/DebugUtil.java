package com.example.hiwin.teacher.BobJavaTester;

public class DebugUtil {

	public static String BytesInHexString(byte[] raw) {
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		for (byte b : raw) {
			sb.append("0x").append(Integer.toHexString(b & 0xFF)).append(",");
		}
		sb.append("}");
		return sb.toString();
	}

	public static void print(String str) {
		System.out.print(str);
	}

	public static void printE(String str) {
		System.err.print(str);
	}

}
