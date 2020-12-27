package com.example.hiwin.teacher_version_bob.protocol.core;

public abstract class Package extends PackageHeader {
	
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

	public byte[] toBytes() {
		byte[] package_data = createEmptyPackageByteArray();
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

	private byte[] createEmptyPackageByteArray() {
		byte[] package_data = new byte[4 + lackBytesLength];
		package_data[0] = (byte) 0xff;
		package_data[1] = (byte) 0xef;
		package_data[2] = (byte) action;
		package_data[3] = (byte) length;
		return package_data;
	}
	
	protected static int toUnsignedInt(byte raw) {
		return 0xff & raw;
	}

	protected static byte[] getPackageHeaderByteArray(byte[] raw) {
		byte[] header = new byte[4];
		System.arraycopy(raw, 0, header, 0, 4);
		return header;
	}

}
