package at.rennweg.htl.netcrawler.test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import at.andiwand.library.network.MACAddress;
import at.andiwand.packettracer.ptmp.simple.SimpleMultiuserClient;
import at.andiwand.packettracer.ptmp.simple.SimpleNetworkDevice;
import at.andiwand.packettracer.ptmp.simple.SimpleNetworkDeviceFactory;
import at.andiwand.packettracer.ptmp.simple.SimpleTelnetConnection;
import at.rennweg.htl.netcrawler.cli.CiscoCommand;
import at.rennweg.htl.netcrawler.cli.CiscoCommandLineExecutor;
import at.rennweg.htl.netcrawler.cli.CiscoMoreCommand;
import at.rennweg.htl.netcrawler.cli.CiscoUser;


public class TestCiscoCommandLineExecutor {
	
	private static SimpleNetworkDevice networkDevice;
	private static final Object networkDeviceSync = new Object();
	
	public static void main(String[] args) throws Throwable {
		SimpleMultiuserClient multiuserClient = new SimpleMultiuserClient();
		multiuserClient.setInterfaceFactory(new SimpleNetworkDeviceFactory() {
			public SimpleNetworkDevice createInterface(String[] linkRequest) {
				if (networkDevice != null) return null;
				
				try {
					String name = "Simple Bridge Interface";
					MACAddress macAddress = new MACAddress("00:24:8c:fd:fe:96");
					Inet4Address inet4Address = (Inet4Address) Inet4Address.getByName("192.168.0.1");
					Inet4Address defaultRoute = (Inet4Address) Inet4Address.getByName("192.168.0.254");
					
					SimpleNetworkDevice device = new SimpleNetworkDevice(name, macAddress, inet4Address);
					device.setDefaultRoute(defaultRoute);
					
					synchronized (networkDeviceSync) {
						networkDevice = device;
						networkDeviceSync.notify();
					}
					
					return device;
				} catch (UnknownHostException e) {}
				
				return null;
			}
		});
		multiuserClient.connect(InetAddress.getLocalHost());
		
		synchronized (networkDeviceSync) {
			if (networkDevice == null) networkDeviceSync.wait();
		}
		
		
		Inet4Address destinationAddress = (Inet4Address) Inet4Address.getByName("192.168.0.254");
		SimpleTelnetConnection telnetConnection = networkDevice.createTelnetConnection(destinationAddress);
		
		CiscoUser user = new CiscoUser("cisco", "cisco");
		CiscoCommandLineExecutor executor = new CiscoCommandLineExecutor(telnetConnection, user);
		
		String[] commands = {"show version"};
		
		for (String commandString : commands) {
			System.out.println("command: " + commandString);
			System.out.println();
			
			CiscoCommand command = executor.execute(commandString);
			CiscoMoreCommand moreCommand = new CiscoMoreCommand(command);
			
			System.out.println(moreCommand.readWholeOutput());
		}
		
		System.out.println();
		System.out.println();
		System.out.println("finish");
		
		executor.close();
		System.exit(0);
	}
	
}