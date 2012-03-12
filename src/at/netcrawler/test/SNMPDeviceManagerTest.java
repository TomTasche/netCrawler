package at.netcrawler.test;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.ip.SubnetMask;
import at.andiwand.library.network.mac.MACAddress;
import at.netcrawler.io.json.JsonClassAdapter;
import at.netcrawler.io.json.JsonIPv4AddressAdapter;
import at.netcrawler.io.json.JsonMACAddressAdapter;
import at.netcrawler.io.json.JsonNetworkModelAdapter;
import at.netcrawler.io.json.JsonSubnetMaskAdapter;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.snmp.LocalSNMPConnection;
import at.netcrawler.network.connection.snmp.SNMPSettings;
import at.netcrawler.network.connection.snmp.SNMPVersion;
import at.netcrawler.network.manager.snmp.SNMPDeviceManager;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class SNMPDeviceManagerTest {
	
	public static void main(String[] args) throws Throwable {
		String address = "192.168.15.4";
		IPv4Address ipAddress = new IPv4Address(address);
		int port = 161;
		
		IPDeviceAccessor accessor = new IPDeviceAccessor(ipAddress);
		
		SNMPSettings settings = new SNMPSettings();
		settings.setVersion(SNMPVersion.VERSION2C);
		settings.setPort(port);
		settings.setCommunity("netCrawler");
		// settings.setSecurityLevel(SNMPSecurityLevel.AUTH_PRIV);
		// settings.setUsername("ciscocisco");
		// settings.setPassword("ciscocisco");
		// settings.setCryptoKey("ciscocisco");
		
		LocalSNMPConnection connection = new LocalSNMPConnection(accessor,
				settings);
		
		NetworkDevice device = new NetworkDevice();
		SNMPDeviceManager deviceManager = new SNMPDeviceManager(device,
				connection);
		
		deviceManager.complete();
		System.out.println(device);
		System.out.println(device.getValue(NetworkDevice.INTERFACES));
		System.out.println(device.getValue(NetworkDevice.MANAGEMENT_ADDRESSES));
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(Class.class, new JsonClassAdapter());
		gsonBuilder.registerTypeAdapter(MACAddress.class,
				new JsonMACAddressAdapter());
		gsonBuilder.registerTypeAdapter(IPv4Address.class,
				new JsonIPv4AddressAdapter());
		gsonBuilder.registerTypeAdapter(SubnetMask.class,
				new JsonSubnetMaskAdapter());
		gsonBuilder.registerTypeHierarchyAdapter(NetworkModel.class,
				new JsonNetworkModelAdapter());
		
		Gson gson = gsonBuilder.create();
		String gsonString = gson.toJson(device);
		System.out.println(gsonString);
		System.out.println(gson.fromJson(gsonString, NetworkDevice.class));
	}
	
}