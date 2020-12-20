package com.example.hiwin.teacher_version_bob.protocol.core.data;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DataPackage extends ArrayList<SplitDataPackage> {
//    static final int SPLIT_SIZE=253;
	static final int SPLIT_SIZE = 50;
	
	public byte[] getData() {
		ArrayList<Byte> newData = new ArrayList<Byte>();

		for (SplitDataPackage splitDataPackage : this) {
			byte[] splitBytes = splitDataPackage.getData();
			for (byte b : splitBytes) {
				newData.add(b);
			}
		}
		byte[] newbytes = new byte[newData.size()];
		for (int i = 0; i < newbytes.length; i++) {
			newbytes[i] = newData.get(i);
		}
		return newbytes;
	}

	public boolean isComplete() {
		int total = this.get(0).getTotal();
		int counter1 = 0;
		for (int i = 0; i < total; i++) {
			counter1 += i;
		}

		int counter2 = 0;
		for (SplitDataPackage splitDataPackage : this) {
			counter2 += splitDataPackage.getIndex();
		}

		return counter1 == counter2;

	}

	public static ArrayList<SplitDataPackage> splitPackage(byte[] data) {
		final ArrayList<SplitDataPackage> dataPackages = new ArrayList<SplitDataPackage>();
		int availableBytes = data.length;
		int index = 0, counter = 0;
		int total = availableBytes / SPLIT_SIZE + Math.min(1, availableBytes % SPLIT_SIZE);

		while (availableBytes > 0) {
			int length = Math.min(availableBytes, SPLIT_SIZE);
			byte[] splited = splitBytes(data, index, length);
			SplitDataPackage splitDataPackage = new SplitDataPackage(splited, counter, total);
			dataPackages.add(splitDataPackage.getIndex(), splitDataPackage);
			availableBytes -= SPLIT_SIZE;
			index += SPLIT_SIZE;
			counter++;
		}
		return dataPackages;
	}

	private static byte[] splitBytes(byte[] raw, int start, int length) {
		byte[] splited = new byte[length];
		for (int i = 0; i < length; i++) {
			splited[i] = raw[start + i];
		}
		return splited;
	}
}
