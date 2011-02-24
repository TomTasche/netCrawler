package at.andiwand.packettracer.ptmp.multiuser;

import java.util.UUID;

import at.andiwand.packettracer.NetworkDevice;
import at.andiwand.packettracer.ptmp.PTMPClient;
import at.andiwand.packettracer.ptmp.PTMPConfiguration;


public class MultiuserManager extends NetworkDevice {
	
	private PTMPConfiguration configuration;
	
	private String user;
	
	private PTMPClient client;
	private boolean connected;
	
	
	public MultiuserManager(String name, PTMPClient client) {
		this(client.getUser(), name, UUID.randomUUID(), client);
	}
	public MultiuserManager(String user, String name, UUID uuid, PTMPClient client) {
		super(name, uuid);
		
		this.user = user;
		
		this.client = client;
	}
	
	
	public PTMPConfiguration getConfiguration() {
		return configuration;
	}
	public String getUser() {
		return user;
	}
	
	
	public void connect() {
		if (connected) diconnect();
		if (!client.isConnected()) throw new RuntimeException("client isn't connected!");
		
		
		
		connected = true;
	}
	
	public void diconnect() {
		if (!connected) return;
		
		connected = false;
	}
	
}