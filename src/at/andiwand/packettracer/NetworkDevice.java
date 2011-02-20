package at.andiwand.packettracer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public abstract class NetworkDevice {
	
	private String name;
	private UUID uuid;
	
	private Set<NetworkInterface> interfaces =
		new HashSet<NetworkInterface>();
	
	
	public NetworkDevice(String name) {
		this(name, UUID.randomUUID());
	}
	public NetworkDevice(String name, UUID uuid) {
		this.name = name;
		this.uuid = uuid;
	}
	
	
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof NetworkDevice)) return false;
		NetworkDevice device = (NetworkDevice) obj;
		
		return uuid.equals(device.uuid);
	}
	@Override
	public String toString() {
		return name;
	}
	
	
	public String getName() {
		return name;
	}
	public UUID getUuid() {
		return uuid;
	}
	public Set<NetworkInterface> getInterfaces() {
		return new HashSet<NetworkInterface>(interfaces);
	}
	
	
	public void addInterface(NetworkInterface networkInterface) {
		if (networkInterface.device != null)
			networkInterface.device.removeInterface(networkInterface);
		
		interfaces.add(networkInterface);
		networkInterface.device = this;
	}
	
	public void removeInterface(NetworkInterface networkInterface) {
		if (interfaces.contains(networkInterface)) return;
		
		interfaces.remove(networkInterface);
		networkInterface.device = null;
	}
	
}