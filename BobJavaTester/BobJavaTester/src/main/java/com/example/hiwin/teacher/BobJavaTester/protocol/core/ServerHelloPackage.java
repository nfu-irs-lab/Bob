package com.example.hiwin.teacher.BobJavaTester.protocol.core;

public class ServerHelloPackage extends Package {

	public static enum StatusCode {
		ALLOW(0xFF), NOT_SUPPORT(0x01), DENY(0x00);

		private final int code;

		StatusCode(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

		public static StatusCode getStatus(byte code) {

			int statusByte = toUnsignedInt(code);
			StatusCode[] statusCodes = values();
			for (StatusCode statusCode : statusCodes) {
				if (statusByte == statusCode.code) {
					return statusCode;
				}
			}
			throw new IllegalArgumentException(code + " is not a status code.");

		}

	}

	private final StatusCode statusCode;

	public ServerHelloPackage(PackageHeader header, byte[] lackBytes) {
		super(header, lackBytes);
		if (action != PackageType.ServerHello.getAction())
			throw new IllegalArgumentException("Not a ServerHelloPackage");
		statusCode = StatusCode.getStatus(getData()[0]);
	}

	public ServerHelloPackage(StatusCode statusCode) {
		super(PackageType.ServerHello.getAction(), setData(statusCode));
		this.statusCode = statusCode;
	}

	private static byte[] setData(StatusCode code) {
		return new byte[] { (byte) code.getCode() };
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}
}
