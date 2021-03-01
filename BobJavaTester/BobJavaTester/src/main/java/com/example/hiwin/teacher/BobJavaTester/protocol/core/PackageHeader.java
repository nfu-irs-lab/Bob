package com.example.hiwin.teacher.BobJavaTester.protocol.core;

public class PackageHeader {
	/**
	 * 0<=action<=255
	 */
	protected final int action;

	/**
	 * For data
	 * 0<=length<=255
	 */
	protected final int length;
	
	protected final int lackBytesLength;

	public PackageHeader(int action, int length) {
		if (action < 0 || action > 255) {
			throw new IllegalArgumentException("action:" + action);
		}
		
		this.action = action;
		this.length = length;
		this.lackBytesLength=length+1;
	}

	public PackageHeader(byte[] header) {
		if(header.length>4||header.length<=0)
			throw new IllegalArgumentException("Header size is incorrect:"+header.length);
		
		if (toUnsignedInt(header[0]) != 0xff || toUnsignedInt(header[1]) != 0xef) {
			throw new IllegalArgumentException("Header is not coincide.");
		}
		action = toUnsignedInt(header[2]);
		length = toUnsignedInt(header[3]);
		this.lackBytesLength=length+1;
	}
	
	protected static int toUnsignedInt(byte raw) {
		return 0xff & raw;
	}
	
	
	public int getAction() {
		return action;
	}
	
	public int getLength() {
		return length;
	}
	public int getlackBytesLength() {
		return lackBytesLength;
	}
	
	public PackageHeader getHeader() {
		return this;
	}
	
}
