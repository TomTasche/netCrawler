package at.rennweg.htl.netcrawler.test;

import java.net.Inet4Address;

import at.rennweg.htl.netcrawler.cli.SimpleCiscoCommandLineExecutor;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.cli.factory.SimpleCiscoCLIFactroy;
import at.rennweg.htl.netcrawler.cli.factory.SimplePacketTracerTelnetFactory;


public class TestSimpleCiscoCommandLineExecutor {
	
	public static void main(String[] args) throws Throwable {
		Inet4Address destinationAddress = (Inet4Address) Inet4Address.getByName("192.168.12.2");
		
		SimpleCiscoUser user = new SimpleCiscoUser("cisco", "cisco");
		Inet4Address deviceAddress = (Inet4Address) Inet4Address.getByName("192.168.0.1");
		Inet4Address deviceGateway = (Inet4Address) Inet4Address.getByName("192.168.0.254");
		SimpleCiscoCLIFactroy cliFactroy = new SimplePacketTracerTelnetFactory(deviceAddress, deviceGateway);
		SimpleCiscoCommandLineExecutor executor = new SimpleCiscoCommandLineExecutor(cliFactroy.getCommandLine(destinationAddress, user));
		
		String[] commands = {"show running-config", "show ip interface brief", "show running-config", "show ip interface brief", "show version"};
		
		for (String commandString : commands) {
			System.out.println("command: " + commandString);
			System.out.println();
			
			System.out.println(executor.execute(commandString));
			System.out.println();
			System.out.println();
		}
		
		System.out.println();
		System.out.println();
		System.out.println("finish");
		
		executor.close();
		System.exit(0);
	}
	
}