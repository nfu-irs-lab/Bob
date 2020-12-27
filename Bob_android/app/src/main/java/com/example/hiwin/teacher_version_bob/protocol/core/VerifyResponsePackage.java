package com.example.hiwin.teacher_version_bob.protocol.core;

public class VerifyResponsePackage extends Package {
	public static enum Verify {
		OK(0xFF), FAIL(0x01);
		private final int code;

		Verify(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
		
		public static Verify getVerify(byte code) {

			int verifyByte = toUnsignedInt(code);
			Verify[] verifylist = values();
			for (Verify verify : verifylist) {
				if (verifyByte == verify.code) {
					return verify;
				}
			}
			
			throw new IllegalArgumentException(code + " is not a verify code.");

		}

	}

	private final Verify verify;

	public VerifyResponsePackage(Verify verify) {
		super(PackageType.VerifyResponse.getAction(), setData(verify));
		this.verify = verify;
	}

	public VerifyResponsePackage(PackageHeader header, byte[] lackBytes) {
		super(header, lackBytes);
		if (action != PackageType.VerifyResponse.getAction())
			throw new IllegalArgumentException("Not a ClientHelloPackage");
		this.verify = Verify.getVerify(getData()[0]);
	}

	public boolean verify() {
		return verify==Verify.OK;
	}
	private static byte[] setData(Verify verify) {
		return new byte[] { (byte) verify.getCode()};
	}

}

