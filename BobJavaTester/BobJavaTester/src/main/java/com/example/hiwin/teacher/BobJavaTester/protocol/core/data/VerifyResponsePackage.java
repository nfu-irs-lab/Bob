package com.example.hiwin.teacher.BobJavaTester.protocol.core.data;

import com.example.hiwin.teacher.BobJavaTester.protocol.core.Package;
import com.example.hiwin.teacher.BobJavaTester.protocol.core.PackageHeader;

public class VerifyResponsePackage extends Package {
	public static enum Verify{
		OK(0xFF),FAIL(0x01);
		private final int code;
		Verify(int code){
			this.code=code;
		}
		public int getCode() {
			return code;
		}
	}
	
	public VerifyResponsePackage(Verify verify) {
		super(Package.Type.ClientHello.getAction(), setData(verify));
	}
	
	public VerifyResponsePackage(PackageHeader header, byte[] lackBytes) {
		super(header, lackBytes);
		if (action!=Package.Type.VerifyResponse.getAction())
            throw new IllegalArgumentException("Not a ClientHelloPackage");
        
	}
	
	private static byte[] setData(Verify verify) {
		return new byte[] {(byte)verify.getCode()};
	}

}
