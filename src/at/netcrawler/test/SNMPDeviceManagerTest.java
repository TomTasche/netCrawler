package at.netcrawler.test;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.snmp.LocalSNMPConnection;
import at.netcrawler.network.connection.snmp.SNMPSettings;
import at.netcrawler.network.connection.snmp.SNMPVersion;
import at.netcrawler.network.manager.snmp.SNMPDeviceManager;
import at.netcrawler.network.model.NetworkDevice;


public class SNMPDeviceManagerTest {
	
	public static void main(String[] args) throws Throwable {
		String address = "192.168.15.1";
		IPv4Address ipAddress = IPv4Address.getByAddress(address);
		int port = 161;
		
		IPDeviceAccessor accessor = new IPDeviceAccessor(ipAddress);
		
		SNMPSettings settings = new SNMPSettings();
		settings.setVersion(SNMPVersion.VERSION2C);
		settings.setPort(port);
		settings.setCommunity("netCrawler");
//		settings.setSecurityLevel(SNMPSecurityLevel.AUTH_PRIV);
//		settings.setUsername("ciscocisco");
//		settings.setPassword("ciscocisco");
//		settings.setCryptoKey("ciscocisco");
		
		LocalSNMPConnection connection = new LocalSNMPConnection(accessor,
				settings);
		
		NetworkDevice device = new NetworkDevice();
		SNMPDeviceManager deviceManager = new SNMPDeviceManager(device,
				connection);
		
		deviceManager.fetchDevice();
		System.out.println(device);
		System.out.println(device.getValue(NetworkDevice.INTERFACES));
		System.out.println(device.getValue(NetworkDevice.MANAGEMENT_ADDRESSES));
	}
	
}