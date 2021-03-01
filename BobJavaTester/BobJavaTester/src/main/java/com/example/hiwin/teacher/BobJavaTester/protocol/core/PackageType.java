package com.example.hiwin.teacher.BobJavaTester.protocol.core;

public enum PackageType {
	ClientHello(0xe0), ServerHello(0xf0), ClientBye(0xe1), SplitData(0x03), VerifyResponse(0x04);
	private final int action;

	private PackageType(int action) {
		this.action = action;
	}

	public int getAction() {
		return action;
	}

	public static PackageType getPackageType(byte[] importBytes) {
		byte[] header_bytes = Package.getPackageHeaderByteArray(importBytes);
		try {
			PackageHeader header = new PackageHeader(header_bytes);
			return getPackageType(header);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PackageType getPackageType(PackageHeader header) {
		PackageType[] types = values();
		for (PackageType type : types) {
			if (header.getAction() == type.getAction()) {
				return type;
			}
		}
		return null;
	}
}