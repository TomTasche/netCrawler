package at.andiwand.packettracer.ptmp.simple;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;


public class SimplePipedTelnetServer extends Thread {
	
	private SimpleNetworkDevice device;
	private Inet4Address ptAdd;
	
	private ServerSocket serverSocket;
	
	
	public SimplePipedTelnetServer(int port, SimpleNetworkDevice device, Inet4Address ptAdd) throws IOException {
		this.device = device;
		this.ptAdd = ptAdd;
		
		serverSocket = new ServerSocket(port);
	}
	
	
	@Override
	public void run() {
		try {
			while (true) {
				Socket socket = serverSocket.accept();
				SimpleTelnetConnection telnetConnection = device.createTelnetConnection(ptAdd);
				
				new Worker(socket, telnetConnection);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private class Worker {
		private SimpleTelnetConnection telnetConnection;
		private Socket socket;
		
		private Thread inputPipe = new Thread() {
			public void run() {
				InputStream inputStream = null;
				OutputStream telnetOutputStream = null;
				
				try {
					inputStream = socket.getInputStream();
					telnetOutputStream = telnetConnection.getOutputStream();
					
					while (true) {
						int read = inputStream.read();
						byte[] buffer = new byte[1 + inputStream.available()];
						if (inputStream.read(buffer, 1, buffer.length -1 ) == -1) break;
						buffer[0] = (byte) read;
						telnetOutputStream.write(buffer);
					}
				} catch (IOException e) {
				} finally {
					try {
						inputStream.close();
						telnetOutputStream.close();
					} catch (Throwable t) {}
				}
			}
		};
		private Thread outputPipe = new Thread() {
			public void run() {
				OutputStream outputStream = null;
				InputStream telnetInputStream = null;
				
				try {
					outputStream = socket.getOutputStream();
					telnetInputStream = telnetConnection.getInputStream();
					
					outputStream.write(new byte[] {(byte) 255, (byte) 251, 1});
					outputStream.write(new byte[] {(byte) 255, (byte) 254, 1});
					outputStream.write(new byte[] {(byte) 255, (byte) 253, 31});
					outputStream.write(new byte[] {(byte) 255, (byte) 251, 3});
					outputStream.write(new byte[] {(byte) 255, (byte) 253, 3});
					outputStream.write(new byte[] {(byte) 255, (byte) 253, 24});
					outputStream.write(new byte[] {(byte) 255, (byte) 253, 39});
					outputStream.flush();
					
					int read;
					while ((read = telnetInputStream.read()) != -1) {
						outputStream.write(read);
						outputStream.flush();
					}
				} catch (IOException e) {
				} finally {
					try {
						outputStream.close();
						telnetInputStream.close();
					} catch (Throwable t) {}
				}
			}
		};
		
		public Worker(Socket socket, SimpleTelnetConnection telnetConnection) {
			this.telnetConnection = telnetConnection;
			this.socket = socket;
			
			inputPipe.start();
			outputPipe.start();
		}
	}
	
}