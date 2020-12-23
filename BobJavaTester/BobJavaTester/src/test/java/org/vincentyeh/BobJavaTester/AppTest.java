package org.vincentyeh.BobJavaTester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;

import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.DataPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.SplitDataPackage;

public class AppTest {
//	@Test
	@Test
	public void ArrayTest() {
		byte[] raw= {1,2,3,4,5};
		System.out.println(BytesInHexString(Arrays.copyOfRange(raw,1,1+3)));
	}
	
	String BytesInHexString(byte[] raw) {
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		for (byte b : raw) {
			sb.append("0x").append(Integer.toHexString(b & 0xFF)).append(",");
		}
		sb.append("}");
		return sb.toString();
	}

}
