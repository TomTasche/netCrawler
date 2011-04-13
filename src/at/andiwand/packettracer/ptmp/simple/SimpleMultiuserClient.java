package at.andiwand.packettracer.ptmp.simple;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import at.andiwand.library.network.MACAddress;
import at.andiwand.library.network.SimpleMACAddressFormat;
import at.andiwand.packettracer.ptmp.PTMPConfiguration;


public class SimpleMultiuserClient {
	
	public static final PTMPConfiguration CONFIGURATION = PTMPConfiguration.DEFAULT;
	
	public static final int DEFAULT_PORT = 38000;
	public static final String DEFAULT_USER = "Simple Bridge";
	
	public static final Charset STRING_CHARSET = Charset.forName("utf-8");
	public static final SimpleDateFormat TIMESTAMP_FORMAT =
		new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleMACAddressFormat MACADDRESS_FORMAT =
		new SimpleMACAddressFormat("XXXX.XXXX.XXXX");
	
	
	
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private SimpleNetworkDeviceFactory deviceFactory;
	
	private Map<Integer, SimpleNetworkDevice> devices = 
		new HashMap<Integer, SimpleNetworkDevice>();
	
	
	
	public void connect(InetAddress address) throws IOException {
		connect(address, DEFAULT_PORT, DEFAULT_USER, "", DEFAULT_USER);
	}
	public void connect(InetAddress address, int port, String remoteNetworkName) throws IOException {
		connect(address, port, DEFAULT_USER, "", remoteNetworkName);
	}
	public void connect(InetAddress address, int port, String username, String password, String remoteNetworkName) throws IOException {
		socket = new Socket(address, port);
		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
		
		connectPtmp(username, password);
		connectMultiuser(username, remoteNetworkName);
		
		new PacketReceiver();
	}
	private void connectPtmp(String username, String password) throws IOException {
		UUID clientUuid = UUID.randomUUID();
		String timestamp = TIMESTAMP_FORMAT.format(new Date());
		
		Object[] negotiationRequest = {"PTMP", 1, clientUuid, CONFIGURATION.encoding(),
				CONFIGURATION.encryption(), CONFIGURATION.compression(),
				CONFIGURATION.authentication(), timestamp, 0, ""};
		
		sendPtmpPacket(0, negotiationRequest);
		
		
		String[] negotiationResponse = readPtmpPacket();
		if (!negotiationResponse[0].equals("1"))
			throw new RuntimeException("illegal type!");
		
		PTMPConfiguration serverConfiguration = new PTMPConfiguration(
				Integer.parseInt(negotiationResponse[4]),
				Integer.parseInt(negotiationResponse[5]),
				Integer.parseInt(negotiationResponse[6]),
				Integer.parseInt(negotiationResponse[7]));
		
		if (!CONFIGURATION.equals(serverConfiguration))
			throw new RuntimeException("illegal config!");
		
		
		Object[] authenticationRequest = {username};
		
		sendPtmpPacket(2, authenticationRequest);
		
		
		String[] authenticationChallenge = readPtmpPacket();
		
		if (!authenticationChallenge[0].equals("3"))
			throw new RuntimeException("illegal type!");
		
		
		Object[] authenticationResponse = {username, password, ""};
		
		sendPtmpPacket(4, authenticationResponse);
		
		
		String[] authenticationStatus = readPtmpPacket();
		if (!authenticationStatus[0].equals("5"))
			throw new RuntimeException("illegal type!");
		
		if (!authenticationStatus[1].equals("true"))
			throw new RuntimeException("authentication failed!");
	}
	private void connectMultiuser(String username, String remoteNetworkName) throws IOException {
		UUID multiuserUuid = UUID.randomUUID();
		
		Object[] multiuserRequest = {username, multiuserUuid};
		
		sendPtmpPacket(200, multiuserRequest);
		
		
		String[] multiuserResponse = readPtmpPacket();
		if (!multiuserResponse[0].equals("201"))
			throw new RuntimeException("illegal type!");
		
		
		Object[] multiuserNetworkRequest = {remoteNetworkName};
		
		sendPtmpPacket(210, multiuserNetworkRequest);
		
		
		String[] packet;
		
		do {
			packet = readPtmpPacket();
			
			if (packet[0].equals("203"))
				handleLinkRequest(packet);
		} while (!packet[0].equals("202"));
		
		
		Object[] multiuserStatusResponse = {0};
		
		sendPtmpPacket(202, multiuserStatusResponse);
	}
	
	public void disconnect() {
		//TODO: implement
	}
	
	
	public void setInterfaceFactory(SimpleNetworkDeviceFactory deviceFactory) {
		this.deviceFactory = deviceFactory;
	}
	
	
	public void createLink(int linkId, UUID linkUuid, int cableType, String linkName) throws IOException {
		Object[] linkRequest = {0, 0, linkId, linkUuid, -1, cableType, linkName, 3, "true",
				"false", "false", 10000, "true", "false", "true", "true", 0, "false",
				1, 1, "false", "false", "true"};
		
		sendPtmpPacket(203, linkRequest);
	}
	
	private void handleLinkRequest(String[] packet) throws IOException {
		int linkId = Integer.parseInt(packet[3]);
		
		if (packet[8].equals("3")) {
			if (deviceFactory == null) return;
			
			SimpleNetworkDevice device = deviceFactory.createInterface(packet);
			if (device == null) return;
			
			if (devices.containsKey(linkId)) {
				SimpleNetworkDevice old = devices.get(linkId);
				old.stop();
				old.setMultiuserClient(null, 0);
			}
			
			device.setMultiuserClient(this, linkId);
			device.start();
			devices.put(linkId, device);
			
			Object[] linkResponse = {0, 0, linkId, packet[4], -1, packet[6], packet[7], packet[8], "true",
					"false", "false", packet[12], packet[13], packet[14], packet[15], packet[16], 0, "false",
					1, 1, "false", "false", packet[23]};
			
			sendPtmpPacket(203, linkResponse);
		}
	}
	
	
	public String[] readPtmpPacket() throws IOException {
		String lengthString = "";
		int read;
		
		while ((read = inputStream.read()) != '\0') {
			lengthString += (char) read;
		}
		
		int length = Integer.valueOf(lengthString);
		
		byte[] packet = new byte[length];
		inputStream.read(packet);
		
		String packetString = new String(packet, STRING_CHARSET);
		String[] fields = packetString.split("\0");
		
		return fields;
	}
	
	public void sendPtmpPacket(int type, Object... packet) throws IOException {
		byte[] tmp = arrayToPtmpPacket(new Object[] {type, packet});
		outputStream.write((tmp.length + "\0").getBytes(STRING_CHARSET));
		outputStream.write(tmp);
		outputStream.flush();
	}
	private byte[] arrayToPtmpPacket(Object[] array) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		for (Object field : array) {
			if (field.getClass().equals(Object[].class)) {
				byte[] buffer = arrayToPtmpPacket((Object[]) field);
				byteArrayOutputStream.write(buffer);
			} else {
				String string;
				
				if (field.getClass().equals(UUID.class)) {
					string = "{" + field + "}";
				} else if (field.getClass().equals(MACAddress.class)) {
					string = MACADDRESS_FORMAT.format(field);
				} else if (field.getClass().equals(Inet4Address.class)) {
					string = ((Inet4Address) field).getHostAddress();
				} else {
					string = field.toString();
				}
				
				byteArrayOutputStream.write((string + "\0").getBytes(STRING_CHARSET));
			}
		}
		
		return byteArrayOutputStream.toByteArray();
	}
	
	public void sendFrame(int linkId, Object[] frame) throws IOException {
		Object[] packet = {0, linkId, frame};
		
		sendPtmpPacket(205, packet);
	}
	
	
	private class PacketReceiver extends Thread {
		public PacketReceiver() {
			start();
		}
		
		public void run() {
			try {
				while (true) {
					String[] packet = readPtmpPacket();
					int type = Integer.parseInt(packet[0]);
					
					switch (type) {
					case 203:
						handleLinkRequest(packet);
						break;
					case 205:
						for (int i = 0; i < packet.length; i++) {
							if (packet[i].equals("CVariableSizePdu")) {
								int size = Integer.parseInt(packet[i + 1]);
								
								String[] tmp = new String[packet.length - size - 3];
								System.arraycopy(packet, 0, tmp, 0, i);
								System.arraycopy(packet, i + 3 + size, tmp, i, tmp.length - i);
								packet = tmp;
								
								break;
							}
						}
						
						int linkId = Integer.parseInt(packet[2]);
						SimpleNetworkDevice ethernetInterface = devices.get(linkId);
						
						String[] networkPacket = new String[packet.length - 3];
						System.arraycopy(packet, 3, networkPacket, 0, networkPacket.length);
						ethernetInterface.queueFrame(networkPacket);
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}