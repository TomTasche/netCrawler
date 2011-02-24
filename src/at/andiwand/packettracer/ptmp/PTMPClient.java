package at.andiwand.packettracer.ptmp;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import at.andiwand.packettracer.ptmp.multiuser.MultiuserManager;
import at.andiwand.packettracer.ptmp.packet.PTMPPacket;
import at.andiwand.packettracer.ptmp.packet.PTMPPacketReader;
import at.andiwand.packettracer.ptmp.packet.PTMPPacketWriter;




public class PTMPClient {
	
	public static final String DEFUALT_USER = "guest";
	
	public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	
	
	private PTMPConfiguration configuration;
	
	private UUID uuid;
	private String user;
	
	private Socket socket;
	private PTMPPacketReader packetReader;
	private PTMPPacketWriter packetWriter;
	private boolean connected;
	
	private MultiuserManager multiuserManager;
	
	
	public PTMPClient() {
		this(UUID.randomUUID(), DEFUALT_USER, PTMPConfiguration.DEFAULT);
	}
	public PTMPClient(UUID uuid, String user, PTMPConfiguration configuration) {
		this.uuid = uuid;
		this.user = user;
		
		socket = new Socket();
	}
	
	
	public PTMPConfiguration getConfiguration() {
		return configuration;
	}
	public UUID getUuid() {
		return uuid;
	}
	public String getUser() {
		return user;
	}
	public boolean isConnected() {
		return connected;
	}
	public MultiuserManager getMultiuserManager() {
		return multiuserManager;
	}
	
	private void setConfiguration(PTMPConfiguration configuration) {
		this.configuration = configuration;
		
		packetReader.setConfiguration(configuration);
		packetWriter.setConfiguration(configuration);
	}
	public void setMultiuserManager(MultiuserManager multiuserManager) {
		this.multiuserManager = multiuserManager;
	}
	
	
	public void connect(InetAddress address, int port, PTMPConfiguration configuration) throws IOException {
		connect(new InetSocketAddress(address, port), configuration);
	}
	public void connect(SocketAddress socketAddress, PTMPConfiguration configuration) throws IOException {
		if (connected) disconnect();
		
		socket.connect(socketAddress);
		
		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();
		
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
		
		packetReader = new PTMPPacketReader(inputStream, PTMPConfiguration.DEFAULT);
		packetWriter = new PTMPPacketWriter(bufferedOutputStream, PTMPConfiguration.DEFAULT);
		
		
		PTMPPacket negotiationRequest = new PTMPPacket(PTMPPacket.TYPE_NEGOTIATION_REQUEST,
				"PTMP", 1, uuid, configuration.encoding(), configuration.encryption(),
				configuration.compression(), configuration.authentication(),
				TIMESTAMP_FORMAT.format(new Date()), 0, "");
		packetWriter.writePacket(negotiationRequest);
		
		PTMPPacket negotiationReply = packetReader.readPacket();
		Object[] negotiationReplyValue = negotiationReply.getValue();
		PTMPConfiguration newConfiguration = new PTMPConfiguration(
				(Integer) negotiationReplyValue[3], (Integer) negotiationReplyValue[4],
				(Integer) negotiationReplyValue[5], (Integer) negotiationReplyValue[6]);
		setConfiguration(newConfiguration);
		
		PTMPPacket authenticationRequest = new PTMPPacket(PTMPPacket.TYPE_AUTHENTICATION_REQUEST,
				user);
		packetWriter.writePacket(authenticationRequest);
		
		//PTMPPacket authenticationChallenge = packetReader.readPacket();
		packetReader.readPacket();
		
		PTMPPacket authenticationResponse = new PTMPPacket(PTMPPacket.TYPE_AUTHENTICATION_RESPONSE,
				user, "", "");
		packetWriter.writePacket(authenticationResponse);
		
		PTMPPacket authenticationStatus = packetReader.readPacket();
		Object[] authenticationStatusValue = authenticationStatus.getValue();
		if (!(Boolean) authenticationStatusValue[0])
			throw new RuntimeException("connection refused!");
		
		connected = true;
	}
	
	public void disconnect() throws IOException {
		if (!connected) return;
		
		socket.close();
		
		connected = false;
	}
	
	
	public void sendPacket(PTMPPacket packet) throws IOException {
		packetWriter.writePacket(packet);
	}
	public PTMPPacket receivePacket() throws IOException {
		return packetReader.readPacket();
	}
	
}