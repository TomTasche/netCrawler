package at.netcrawler.test;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.snmp.LocalSNMPConnection;
import at.netcrawler.network.connection.snmp.SNMPConnectionSettings;
import at.netcrawler.network.connection.snmp.SNMPSecurityLevel;
import at.netcrawler.network.manager.snmp.SNMPDeviceManager;
import at.netcrawler.network.model.NetworkDevice;


public class SNMPDeviceManagerTest {
	
	public static void main(String[] args) throws Throwable {
		String address = "192.168.1.5";
		IPv4Address ipAddress = IPv4Address.getByAddress(address);
		int port = 161;
		
		IPDeviceAccessor accessor = new IPDeviceAccessor(ipAddress);
		
		SNMPConnectionSettings settings = new SNMPConnectionSettings();
		settings.setPort(port);
		settings.setSecurityLevel(SNMPSecurityLevel.AUTH_PRIV);
		settings.setUsername("ciscocisco");
		settings.setPassword("ciscocisco");
		settings.setCryptoKey("ciscocisco");
		
		LocalSNMPConnection connection = new LocalSNMPConnection(accessor,
				settings);
		
		NetworkDevice device = new NetworkDevice();
		SNMPDeviceManager deviceManager = new SNMPDeviceManager(device,
				connection);
		
		deviceManager.fetchDevice();
		System.out.println(device);
		
		deviceManager.setHostname("HAHA");
		
		deviceManager.fetchDevice();
		System.out.println(device);
	}
	
}