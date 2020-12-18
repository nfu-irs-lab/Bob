package com.example.hiwin.teacher.BobJavaTester.protocol.core;

public abstract class Package extends PackageHeader {
	public static enum Type {
		ClientHello(0xe0), ServerHello(0xf0), ClientBye(0xe1), SplitData(0x03),VerifyResponse(0x04);
		private final int action;
		
		private Type(int action) {
			this.action = action;
		}

		public int getAction() {
			return action;
		}

		public static Type getPackageType(byte[] importBytes) {
			byte[] header_bytes = getPackageHeader(importBytes);
			PackageHeader header = new PackageHeader(header_bytes);
			return getPackageType(header);
		}

		public static Type getPackageType(PackageHeader header) {
			Type[] types = values();
			for (Type type : types) {
				if (header.getAction() == type.getAction()) {
					return type;
				}
			}
			return null;
		}
	}

	/**
	 * All element: -128~127
	 */
	private final byte[] data;

	/**
	 * @param action 0<=action<=255
	 * @param data
	 */
	public Package(int action, byte[] data) {
		super(action, data.length);
		this.data = data;
	}

	public Package(PackageHeader header, byte[] lackBytes) {
		super(header.action, header.length);
		data = new byte[length];
		int cksum_real = action + length;
		int cksum = toUnsignedInt(lackBytes[lackBytes.length - 1]);

		for (int i = 0; i < lackBytes.length - 1; i++) {
			data[i] = lackBytes[i];

			cksum_real += data[i];
		}

		cksum_real = 0xff & cksum_real;

		if (cksum_real != cksum) {
			throw new IllegalArgumentException("Cksum is not coincide.");
		}
	}

//	@Deprecated
//	public Package(byte[] importBytes) {
//		super(getPackageHeader(importBytes));
//		int cksum = toUnsignedInt(importBytes[importBytes.length - 1]);
//		int cksum_real = action + length;
//
//		data = new byte[length];
//
//		for (int i = 0; i < length; i++) {
//			data[i] = importBytes[4 + i];
//			cksum_real += data[i];
//		}
//
//		cksum_real = 0xff & cksum_real;
//
//		if (cksum_real != cksum) {
//			throw new IllegalArgumentException("Cksum is not coincide.");
//		}
//
//	}

	public byte[] toBytes() {
		byte[] package_data = createHeaderByteArray();
		int cksum = action + length;

		for (int i = 0; i < length; i++) {
			package_data[4 + i] = data[i];
			cksum += data[i];
		}
		package_data[package_data.length - 1] = (byte) cksum;
		return package_data;
	}

	protected byte[] getData() {
		return data;
	}

	protected static int toUnsignedInt(byte raw) {
		return 0xff & raw;
	}

	protected static byte[] getPackageHeader(byte[] raw) {
		byte[] header = new byte[4];
		System.arraycopy(raw, 0, header, 0, 4);
		return header;
	}
}
