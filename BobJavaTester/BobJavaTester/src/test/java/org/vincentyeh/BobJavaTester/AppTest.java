package org.vincentyeh.BobJavaTester;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.DataPackage;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.data.SplitDataPackage;

public class AppTest {
	@Test
	public void testApp() {
		int availableBytes = 5;
		int SPLIT_SIZE = 2;
		int total = availableBytes / SPLIT_SIZE + Math.min(1, availableBytes % SPLIT_SIZE);
		System.out.println(total);
	}

	@Test
	public void testLinkList() {
		ArrayList<SplitDataPackage> datas = DataPackage.splitPackage(new byte[] {1,2,3,4,5,6,7});

		LinkedList<SplitDataPackage> tasks = new LinkedList<SplitDataPackage>(datas);
		
		while(!tasks.isEmpty()) {
			System.out.println(BytesInHexString(tasks.getFirst().getData()));
			tasks.removeFirst();
		}
		
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
