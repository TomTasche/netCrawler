package at.andiwand.packettracer.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;


public class TestSimpleMultiuserClient {
	
	public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	
	
	public static void main(String[] args) throws IOException {
		InetAddress address = InetAddress.getByName("localhost");
		int port = 38000;
		
		String uuid = "{" + UUID.randomUUID() + "}";
		int encoding = 1;
		int encryption = 1;
		int compression = 1;
		int authentication = 1;
		String timestamp = TIMESTAMP_FORMAT.format(new Date());
		
		String username = "Peer0";
		String password = "";
		
		String remoteNetworkName = "Andi :D";
		
		
		Socket socket = new Socket(address, port);
		System.out.println("opened connection");
		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();
		
		byte[] tmp;
		int length;
		byte[] packet;
		int read;
		
		
		tmp = (
				"0" + "\0" +
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
			System.out.println(packetString.replaceAll("\0", "|"));
			
			int type = Integer.valueOf(fields[0]);
			
			System.out.println("packet type " + type);
			
			switch (type) {
				case 1:
					encoding = Integer.valueOf(fields[4]);
					encryption = Integer.valueOf(fields[5]);
					compression = Integer.valueOf(fields[6]);
					authentication = Integer.valueOf(fields[7]);
					
					tmp = (
							"2" + "\0" +
							username + "\0"
					).getBytes();
					
					outputStream.write((tmp.length + "\0").getBytes());
					outputStream.write(tmp);
					outputStream.flush();
					
					break;
				case 3:
					String challengeText = fields[1];
					System.out.println("challenge text: " + challengeText);
					
					String digestText = password;
					
					tmp = (
							"4" + "\0" +
							username + "\0" +
							digestText + "\0" +
							"\0"
					).getBytes();
					
					outputStream.write((tmp.length + "\0").getBytes());
					outputStream.write(tmp);
					outputStream.flush();
					
					break;
				case 5:
					String status = fields[1];
					System.out.println("authetification status: " + status);
					if (!status.equals("true")) System.exit(0);
					
					String clientUuid = "{" + UUID.randomUUID() + "}";
					
					tmp = (
							"200" + "\0" +
							"Guest" + "\0" +
							clientUuid + "\0"
					).getBytes();
					
					outputStream.write((tmp.length + "\0").getBytes());
					outputStream.write(tmp);
					outputStream.flush();
					
					break;
				
				case 201:
					tmp = (
							"210" + "\0" +
							remoteNetworkName + "\0"
					).getBytes();
					
					outputStream.write((tmp.length + "\0").getBytes());
					outputStream.write(tmp);
					outputStream.flush();
					
					break;
				case 202:
					tmp = (
							"202" + "\0" +
							"0" + "\0"
					).getBytes();
					
					outputStream.write((tmp.length + "\0").getBytes());
					outputStream.write(tmp);
					outputStream.flush();
					
					break;
				case 203:
					tmp = (
							"203" + "\0" +
							fields[1] + "\0" +						// ka
							fields[2] + "\0" +						// ka
							fields[3] + "\0" +						// link number
							fields[4] + "\0" +						// link uuid
							"-1" + "\0" +							// ka
							fields[6] + "\0" +						// calble type (1 = staight through, 2 = cross over)
							"Andis interface" + "\0" +				// link name
							fields[8] + "\0" +						// link type (3 = fast ethernet)
							"true" + "\0" +							// ka
							"false" + "\0" +						// ka
							"false" + "\0" +						// ka
							fields[12] + "\0" +						// bandwidth
							fields[13] + "\0" +						// duplex (true = full, false = half)
							fields[14] + "\0" +						// ka
							fields[15] + "\0" +						// auto bandwidth
							fields[16] + "\0" +						// auto duplex
							"0" + "\0" +							// ka
							"false" + "\0" +						// ka
							"1" + "\0" +							// ka
							"1" + "\0" +							// ka
							"false" + "\0" +						// ka
							"false" + "\0" +						// ka
							fields[23] + "\0"						// exists
					).getBytes();
					
					outputStream.write((tmp.length + "\0").getBytes());
					outputStream.write(tmp);
					outputStream.flush();
					
					break;
				case 205:
					System.out.println("received data packet");
					String layer = fields[3];
					
					if (layer.equals("CEtherentIIHeader")) {
						System.out.println("... ethernet 2 frame");
						layer = fields[4];
						
						if (layer.equals("CArpPacket")) {
							System.out.println("... arp packet");
							System.out.println("	source mac: " + fields[10]);
							System.out.println("	target mac: " + fields[11]);
							System.out.println("	source ip: " + fields[12]);
							System.out.println("	target ip: " + fields[13]);
							
							String operation = fields[9];
							
							if (operation.equals("1")) {
								System.out.println("... request operation");
								
								if (fields[12].equals(fields[13])) break;
								
								tmp = (
										"205" + "\0" +
										(new Random().nextInt()) + "\0" +
										"0" + "\0" +
										"CEtherentIIHeader" + "\0" +
										"CArpPacket" + "\0" +
										fields[5] + "\0" +
										fields[6] + "\0" +
										fields[7] + "\0" +
										fields[8] + "\0" +
										"2" + "\0" +
										"0024.8cfd.fe96" + "\0" +
										fields[10] + "\0" +
										fields[13] + "\0" +
										fields[12] + "\0" +
										"0024.8cfd.fe96" + "\0" +
										fields[10] + "\0" +
										"0" + "\0" +
										"2054" + "\0"
								).getBytes();
								
								outputStream.write((tmp.length + "\0").getBytes());
								outputStream.write(tmp);
								outputStream.flush();
								
								System.out.println("reply sended");
							}
						} else if (layer.equals("CIpHeader")) {
							System.out.println("... ip packet");
							layer = fields[5];
							
							if (layer.equals("CIcmpMessage")) {
								System.out.println("... icmp message");
								layer = fields[6];
								
								if (layer.equals("CVariableSizePdu")) {
									String icmpType = fields[109];
									
									if (icmpType.equals("8")) {
										System.out.println("... request type");
										System.out.println("	source ip: " + fields[126]);
										System.out.println("	target ip: " + fields[127]);
										
										tmp = (
												"205" + "\0" +
												(new Random().nextInt()) + "\0" +
												"0" + "\0" +
												"CEtherentIIHeader" + "\0" +
												"CIpHeader" + "\0" +
												"CIcmpMessage" + "\0" +
												"CVariableSizePdu" + "\0" +
												"100" + "\0" +
												"100" + "\0" +
												"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0" +
												"0" + "\0" +
												"0" + "\0" +
												"0" + "\0" +
												fields[112] + "\0" +
												fields[113] + "\0" +
												"4" + "\0" +
												"5" + "\0" +
												"0" + "\0" +
												"128" + "\0" +
												"27" + "\0" +
												"0" + "\0" +
												"0" + "\0" +
												"128" + "\0" +
												"1" + "\0" +
												"0" + "\0" +
												"0" + "\0" +
												"0" + "\0" +
												fields[127] + "\0" +
												fields[126] + "\0" +
												"0024.8cfd.fe96" + "\0" +
												fields[128] + "\0" +
												"0" + "\0" +
												"2048" + "\0"
										).getBytes();
										
										outputStream.write((tmp.length + "\0").getBytes());
										outputStream.write(tmp);
										outputStream.flush();
										
										System.out.println("echo reply sended");
									}
								}
							} else if (layer.equals("CTcpHeader")) {
								System.out.println("... tcp");
								
								
							}
						}
					}
					
					break;
				
				case 7:
					System.out.println("disconnected");
					
					if (fields.length > 1) {
						System.out.println("message: " + fields[1]);
					}
					
					System.exit(0);
					
					break;
			}
			
			
			System.out.println();
			System.out.println("----------");
		}
	}
	
}