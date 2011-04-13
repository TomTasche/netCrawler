package at.rennweg.htl.netcrawler.cli.factory;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import at.andiwand.library.network.MACAddress;
import at.andiwand.library.util.cli.CommandLine;
import at.andiwand.packettracer.ptmp.simple.SimpleMultiuserClient;
import at.andiwand.packettracer.ptmp.simple.SimpleNetworkDevice;
import at.andiwand.packettracer.ptmp.simple.SimpleNetworkDeviceFactory;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoLoginManager;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;


public class SimplePacketTracerTelnetFactory implements SimpleCiscoCLIFactroy {
	
	public static final String DEVICE_NAME = "Simple Bridge Interface";
	
	public static final int DEFAULT_PORT = SimpleMultiuserClient.DEFAULT_PORT;
	public static final String DEFAULT_NETWORK_NAME = SimpleMultiuserClient.DEFAULT_USER;
	public static final MACAddress DEFAULT_MAC_ADDRESS = new MACAddress("00:24:8c:fd:fe:96");
	
	
	private SimpleNetworkDevice networkDevice;
	private final Object networkDeviceSync = new Object();
	
	public SimplePacketTracerTelnetFactory(Inet4Address deviceAddress, Inet4Address deviceGateway) throws UnknownHostException, IOException, InterruptedException {
		this(InetAddress.getLocalHost(), DEFAULT_PORT, DEFAULT_NETWORK_NAME, DEFAULT_MAC_ADDRESS, deviceAddress, deviceGateway);
	}
	public SimplePacketTracerTelnetFactory(InetAddress ptAddress, int ptPort, final String networkName, final MACAddress macAddress, final Inet4Address deviceAddress, final Inet4Address deviceGateway) throws IOException, InterruptedException {
		SimpleMultiuserClient multiuserClient = new SimpleMultiuserClient();
		multiuserClient.setInterfaceFactory(new SimpleNetworkDeviceFactory() {
			public SimpleNetworkDevice createInterface(String[] linkRequest) {
				if (networkDevice != null) return null;
				
				SimpleNetworkDevice device = new SimpleNetworkDevice(DEVICE_NAME, macAddress, deviceAddress);
				device.setDefaultRoute(deviceGateway);
				
				synchronized (networkDeviceSync) {
					networkDevice = device;
					networkDeviceSync.notify();
				}
				
				return device;
			}
		});
		multiuserClient.connect(ptAddress, ptPort, networkName);
		
		synchronized (networkDeviceSync) {
			if (networkDevice == null) networkDeviceSync.wait();
		}
	}
	
	
	@Override
	public CommandLine getCommandLine(InetAddress address, SimpleCiscoUser user) throws Exception {
		synchronized (networkDeviceSync) {
			if (networkDevice == null) networkDeviceSync.wait();
		}
		
		CommandLine result = networkDevice.createTelnetConnection((Inet4Address) address);
		
		if (user != null) {
			SimpleCiscoLoginManager loginManager = new SimpleCiscoLoginManager(result);
			
			if (!loginManager.login(user)) {
				//TODO: create new exception
				throw new RuntimeException("Login failed!");
			}
		}
		
		return result;
	}
	
}