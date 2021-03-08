package com.example.hiwin.teacher.BobJavaTester;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedList;

public class Receiver implements Runnable {
	private final ReceiveListener listener;
	private LinkedList<Byte> pre_data = new LinkedList<Byte>();
	private final InputStream is;
	private boolean run;

	public Receiver(InputStream is, ReceiveListener listener) {
		this.is = is;
		this.listener = listener;
		run = true;
	}

	public void run() {
		while (run) {

			try {
				while (is.available() > 0) {
					byte[] b = new byte[1];
					is.read(b);
					pre_data.add(b[0]);
				}

			} catch (IOException e) {
				close();
				e.printStackTrace();
			}

			LinkedList<Byte> buffer = new LinkedList<Byte>(pre_data);
			
			StringBuffer sb;
			do {
				sb = new StringBuffer();
				int original_size = buffer.size();
				for (int i = 0; i < original_size; i++) {
					char chr = (char) (byte) buffer.pollFirst();
					if (chr == '\n') {
						pre_data = buffer;
						final String recv_data = sb.toString();
						byte[] raw = Base64.getUrlDecoder().decode(recv_data);
						String rawStr = new String(raw, StandardCharsets.UTF_8);
						listener.onReveive(rawStr);
						sb = null;
						break;

					} else {
						sb.append(chr);
					}
				}
			} while (sb == null);
		}
	}
	public void close() {
		try {
			this.is.close();
		} catch (IOException e) {
		}
		
		run=false;
		
	}
	
	public static interface ReceiveListener {
		public void onReveive(String str);
	}

}
