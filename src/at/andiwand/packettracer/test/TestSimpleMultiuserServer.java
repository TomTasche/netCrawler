package at.andiwand.packettracer.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class TestSimpleMultiuserServer {
	
	public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	
	
	public static void main(String[] args) throws IOException {
		int port = 38001;
		ServerSocket serverSocket = new ServerSocket(port);
		
		while (true) {
			Socket socket = serverSocket.accept();
			new Worker(socket).start();
		}
	}
	
	
	private static class Worker extends Thread {
		private Socket socket;
		
		public Worker(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			byte[] tmp;
			int length;
			byte[] packet;
			int read;
			
			try {
				String uuid = "{" + UUID.randomUUID() + "}";
				int encoding = 1;
				int encryption = 1;
				int compression = 1;
				int authentication = 1;
				String timestamp = TIMESTAMP_FORMAT.format(new Date());
				
				
				System.out.println("opened connection");
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();
				
				System.out.println();
				System.out.println();
				System.out.println("--- start worker loop ---");
				System.out.println();
				System.out.println();
				
				while (true) {
					String lengthString = "";
					while ((read = inputStream.read()) != '\0') {
						lengthString += (char) read;
					}
					length = Integer.valueOf(lengthString);
					
					packet = new byte[length];
					inputStream.read(packet);
					
					String packetString = new String(packet);
					String[] fields = new String(packet).split("\0");
					
					System.out.println("received packet with " + length + " bytes");
					System.out.println(packetString.replaceAll("\0", "\\."));
					
					int type = Integer.valueOf(fields[0]);
					
					System.out.println("packet type " + type);
					
					switch (type) {
						case 0:
							authentication = 4;
							
							tmp = (
									"1" + "\0" +
									"PTMP" + "\0" +
									"1" + "\0"+
									uuid + "\0" +
									encoding + "\0" +
									encryption + "\0" +
									compression + "\0" +
									authentication + "\0" +
									timestamp + "\0" +
									"0" + "\0" +
									"\0"
							).getBytes();
							
							outputStream.write((tmp.length + "\0").getBytes());
							outputStream.write(tmp);
							outputStream.flush();
							
							break;
						case 7:
							String message = fields[1];
							System.out.println("disconnect: " + message);
							System.exit(0);
							
							break;
					}
					
					
					System.out.println();
					System.out.println("----------");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}