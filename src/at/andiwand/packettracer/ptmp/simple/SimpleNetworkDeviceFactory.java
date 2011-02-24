package at.andiwand.packettracer.ptmp.simple;


public interface SimpleNetworkDeviceFactory {
	
	public SimpleNetworkDevice createInterface(String[] linkRequest);
	
}