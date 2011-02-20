package at.andiwand.packettracer.ptmp.simple2;


public interface SimpleNetworkDeviceFactory {
	
	public SimpleNetworkDevice createInterface(String[] linkRequest);
	
}