package at.rennweg.htl.netcrawler.test;

import java.net.Inet4Address;
import java.net.InetAddress;

import at.andiwand.library.util.cli.CommandLine;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoCommandLineExecutor;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.cli.factory.SimpleCLIFactroy;
import at.rennweg.htl.netcrawler.cli.factory.SimplePTTelnetFactory;
import at.rennweg.htl.netcrawler.network.agent.DefaultCiscoDeviceAgent;
import at.rennweg.htl.netcrawler.network.graph.CiscoDevice;


public class TestDefaultCiscoDeviceAgent {
	
	public static void main(String[] args) throws Throwable {
		SimpleCiscoUser user = new SimpleCiscoUser("cisco", "cisco");
		Inet4Address deviceAddress = (Inet4Address) Inet4Address.getByName("192.168.0.1");
		Inet4Address deviceGateway = (Inet4Address) Inet4Address.getByName("192.168.0.254");
		SimpleCLIFactroy cliFactroy = new SimplePTTelnetFactory(deviceAddress, deviceGateway);
		
		InetAddress destinationAddress = InetAddress.getByName("192.168.0.254");
		
		//asdf
		//int tries = 30;
		//int finished = 0;
		
		//for (int i = 0; i < tries; i++) {
		//	System.out.println("step " + i);
			
			try {
				CommandLine cli = cliFactroy.getCommandLine(destinationAddress, user);
				SimpleCiscoCommandLineExecutor executor = new SimpleCiscoCommandLineExecutor(cli);
				
				DefaultCiscoDeviceAgent deviceAgent = new DefaultCiscoDeviceAgent(executor);
				CiscoDevice device = deviceAgent.fetchAll();
				System.out.println(device.getClass());
				System.out.println(device.getHostname());
				System.out.println(device.getSeriesNumber());
				System.out.println(device.getProcessorBoardId());
				System.out.println(device.getInterfaces());
				System.out.println(device.getManagementAddresses());
				
				cli.close();
				
		//		finished++;
			} catch (Throwable t) {
				t.printStackTrace();
			}
			
		//	System.out.println("step " + i + " done");
		//}
		
		//double percentage = (double) finished / tries * 100;
		//System.out.format("%f%%", percentage);
		
		System.exit(0);
	}
	
}