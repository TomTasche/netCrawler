package at.andiwand.packettracer.ptmp.simple;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Inet4Address;

import at.andiwand.library.util.cli.CommandLine;


public class SimpleTelnetConnection implements CommandLine {
	
	private SimpleNetworkDevice device;
	private int sourcePort;
	private Inet4Address destinationAddress;
	private int destinationPort;
	
	private int sequence;
	private int acknowledgement;
	
	private PipedOutputStream inputStreamWriter = new PipedOutputStream();
	private PipedInputStream inputStream = new PipedInputStream(inputStreamWriter);
	private SimpleTelnetOutputStream outputStream = new SimpleTelnetOutputStream();
	private boolean directWrite;
	
	private Object connectSync = new Object();
	private boolean connected;
	private boolean closed;
	
	
	public SimpleTelnetConnection(SimpleNetworkDevice device, int sourcePort, Inet4Address destinationAddress, int destinationPort) throws IOException {
		this.device = device;
		this.sourcePort = sourcePort;
		this.destinationAddress = destinationAddress;
		this.destinationPort = destinationPort;
		
		if (device.telnetConnections.containsKey(sourcePort))
			throw new RuntimeException("port already in use!");
		device.telnetConnections.put(sourcePort, this);
	}
	
	
	public InputStream getInputStream() {
		synchronized (connectSync) {
			if (connected) return inputStream;
			
			try {
				connectSync.wait();
			} catch (InterruptedException e) {}
		}
		
		return inputStream;
	}
	public OutputStream getOutputStream() {
		synchronized (connectSync) {
			if (connected) return outputStream;
			
			try {
				connectSync.wait();
			} catch (InterruptedException e) {}
		}
		
		return outputStream;
	}
	
	
	public synchronized void connect() throws IOException {
		if (connected) return;
		
		Object[] tcpSyn = {"CTcpHeader", "", sourcePort, destinationPort, 0, sequence, acknowledgement,
				0, 15, 2, -1,0, 0, 0, 1, "CTcpOptionMSS", 2, 4, 1460};
		
		sendSegment(tcpSyn);
		sequence++;
		
		synchronized (connectSync) {
			if (connected) return;
			
			try {
				connectSync.wait();
			} catch (InterruptedException e) {}
		}
	}
	
	
	public int getSourcePort() {
		return sourcePort;
	}
	public Inet4Address getDestinationAddress() {
		return destinationAddress;
	}
	public int getDestinationPort() {
		return destinationPort;
	}
	
	
	public void handleSegment(String[] segment) throws IOException {
		int seq;
		//int ack;
		int flags;
		
		if (segment[1].equals("CTelnetPacket")) {
			seq = Integer.parseInt(segment[8]);
			//ack = Integer.parseInt(segment[9]);
			flags = Integer.parseInt(segment[12]);
		} else if (segment[1].equals("CPduGroup")) {
			int count = Integer.parseInt(segment[2]);
			seq = Integer.parseInt(segment[2 + count * 4 + 4]);
			//ack = Integer.parseInt(segment[2 + count * 4 + 5]);
			flags = Integer.parseInt(segment[2 + count * 4 + 8]);
		} else {
			seq = Integer.parseInt(segment[5]);
			//ack = Integer.parseInt(segment[6]);
			flags = Integer.parseInt(segment[9]);
		}
		
		//sequence = ack;
		acknowledgement = seq + 1;
		
		if (flags == 0x10) return;
		
		Object[] tcpAck = {"CTcpHeader", "", sourcePort, destinationPort, 0, sequence, acknowledgement,
				0, 15, 16, -1, 0, 0, 0, 0};
		
		sendSegment(tcpAck);
		
		if ((flags & 0x02) != 0) {
			synchronized (connectSync) {
				connected = true;
				connectSync.notifyAll();
			}
		}
		
		if ((flags & 0x01) != 0) {
			close();
		}
		if ((flags & 0x04) != 0) {
			close();
		}
		if ((flags & 0x08) != 0) {
			String data;
			boolean printable = false;
			int position = 0;
			
			if (segment[1].equals("CPduGroup")) {
				data = "";
				int count = Integer.parseInt(segment[2]);
				
				for (int i = 0; i < count; i++) {
					data += segment[4 + i * 4];
					printable |= Boolean.parseBoolean(segment[5 + i * 4]);
					if (printable) position = Integer.parseInt(segment[6 + i * 4]);
				}
			} else {
				data = segment[2];
				printable = Boolean.parseBoolean(segment[3]);
				position = Integer.parseInt(segment[4]);
			}
			
			if (printable) {
				directWrite = position > 0;
				
				data = data.replaceAll("\n", "\r\n");
				inputStreamWriter.write(data.getBytes("us-ascii"));
				inputStreamWriter.flush();
			} else {
				for (int i = 0; i < data.length(); i++) {
					char c = data.charAt(i);
					
					switch (c) {
					case 0x08:
						inputStreamWriter.write(new byte[] {(byte) 255, (byte) 247});
						inputStreamWriter.flush();
						break;
					}
				}
			}
		}
	}
	
	public void sendSegment(Object[] segment) throws IOException {
		device.sendPacket(6, destinationAddress, segment);
	}
	
	
	public void close() {
		if (closed) return;
		
		Object[] tcpRst = {"CTcpHeader", "", sourcePort, destinationPort, 0, sequence, acknowledgement,
				0, 15, 4, -1, 0, 0, 0, 0};
		
		try {
			sendSegment(tcpRst);
			
			inputStreamWriter.close();
			inputStream.close();
			outputStream.close();
		} catch (IOException e) {}
		
		device.telnetConnections.remove(sourcePort);
		closed = true;
	}
	
	
	private class SimpleTelnetOutputStream extends OutputStream {
		public void write(int b) throws IOException {
			write(new byte[] {(byte) b}, 0, 1);
		}
		public void write(byte[] b) throws IOException {
			write(b, 0, b.length);
		}
		public void write(byte[] b, int off, int len) throws IOException {
			if (len == 0) return;
			
			String data = new String(b, off, len);
			data = data.replaceAll("\0", "\n");
			
			if (data.contains("" + (char) 65533)) return;
			
			Object[] tcpPsh = {"CTcpHeader", "CTelnetPacket", data, true, 0, sourcePort, destinationPort,
					0, sequence, acknowledgement, 0, 15, 24, -1, 0, 0, 1, 0};
			
			sendSegment(tcpPsh);
			sequence += 108;
			
			if (directWrite) {
				inputStreamWriter.write(b, off, len);
				inputStreamWriter.flush();
			}
		}
	}
	
}