package org.vincentyeh.BobJavaTester;

import org.junit.Test;

public class NewPackageTester {
	
	@Test
	public void verifyPackageData() {
		NewPackage package1=new NewPackage(0xE1,new byte[]{1,2,3,4,5,(byte) 0xEF,(byte) 0xFF}) ;
		dumpBytesInHex(package1.toBytes());
		
		NewPackage package2=new NewPackage(package1.toBytes()) ;
		dumpBytesInHex(package2.toBytes());
	}
	
	@Test
	public void verifyClientHelloData() {
		NewClientHelloPackage package1=new NewClientHelloPackage();
		
		dumpBytesInHex(package1.toBytes());
		
		NewClientHelloPackage package2=new NewClientHelloPackage();
		
		dumpBytesInHex(package2.toBytes());
	}
	
	void dumpBytesInHex(byte[] raw) {
		System.out.print("[");
		for(byte b:raw) {
			System.out.print("0x");
			System.out.print(Integer.toString(toUnsignedInt(b),16));	
			System.out.print(" ");
		}
		System.out.print("]\n");
	}
	
	int toUnsignedInt(byte raw) {
    	return 0xff&raw;
    }

}
