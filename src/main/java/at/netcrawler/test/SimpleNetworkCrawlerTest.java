package at.netcrawler.test;

import java.io.IOException;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.network.connection.ConnectionGateway;
import at.netcrawler.network.connection.ssh.LocalSSHGateway;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;
import at.netcrawler.network.crawler.SimpleNetworkCrawler;
import at.netcrawler.network.manager.DeviceManagerFactory;
import at.netcrawler.network.manager.cli.CommandLineDeviceManagerFactory;
import at.netcrawler.network.topology.HashTopology;
import at.netcrawler.network.topology.Topology;


public class SimpleNetworkCrawlerTest {
	
	public static void main(String[] args) throws IOException {
		ConnectionGateway gateway = new LocalSSHGateway();
		SSHSettings settings = new SSHSettings();
		settings.setVersion(SSHVersion.VERSION2);
		settings.setUsername("cisco");
		settings.setPassword("cisco");
		DeviceManagerFactory managerFactory = new CommandLineDeviceManagerFactory();
		IPv4Address start = new IPv4Address("192.168.0.254");
		SimpleNetworkCrawler crawler = new SimpleNetworkCrawler(gateway,
				settings, managerFactory, start);
		
		Topology topology = new HashTopology();
		crawler.crawl(topology);
		System.out.println(topology);
	}
	
}