package at.andiwand.packettracer.test;

import java.net.InetAddress;

import at.andiwand.packettracer.ptmp.PTMPClient;
import at.andiwand.packettracer.ptmp.PTMPConfiguration;


public class TestPTMPClient {
	
	public static void main(String[] args) throws Throwable {
		InetAddress address = InetAddress.getByName("localhost");
		int port = 38000;
		PTMPConfiguration configuration = new PTMPConfiguration(
				PTMPConfiguration.ENCODING_BINARY, PTMPConfiguration.ENCRYPTION_NONE,
				PTMPConfiguration.COMPRESSION_NO, PTMPConfiguration.AUTHENTICATION_CLEAR_TEXT);
		
		PTMPClient client = new PTMPClient();
		client.connect(address, port, configuration);
	}
	
}