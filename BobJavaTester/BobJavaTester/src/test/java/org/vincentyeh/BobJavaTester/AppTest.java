package org.vincentyeh.BobJavaTester;

import org.junit.Test;

public class AppTest {
	@Test
	public void testApp() {
		int availableBytes=5;
		int SPLIT_SIZE=2;
		int total = availableBytes / SPLIT_SIZE + Math.min(1, availableBytes % SPLIT_SIZE);
		System.out.println(total);
	}
}
