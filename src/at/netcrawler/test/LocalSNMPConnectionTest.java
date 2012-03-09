package at.netcrawler.test;

import java.util.Arrays;
import java.util.List;

import at.andiwand.library.network.ip.IPAddress;
import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.util.ObjectIdentifier;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.snmp.LocalSNMPConnection;
import at.netcrawler.network.connection.snmp.SNMPEntry;
import at.netcrawler.network.connection.snmp.SNMPSettings;
import at.netcrawler.network.connection.snmp.SNMPVersion;


public class LocalSNMPConnectionTest {
	
	public static void main(String[] args) throws Throwable {
		SNMPVersion version = SNMPVersion.VERSION2C;
		String address = "192.168.15.1";
		int port = 161;
		String community = "netCrawler";
		
		IPAddress ipAddress = new IPv4Address(address);
		
		IPDeviceAccessor accessor = new IPDeviceAccessor(ipAddress);
		
		SNMPSettings settings = new SNMPSettings();
		settings.setVersion(version);
		settings.setPort(port);
		settings.setCommunity(community);
		
		LocalSNMPConnection snmp = new LocalSNMPConnection(accessor, settings);
		System.out.println(snmp.get(
				new ObjectIdentifier("1.3.6.1.2.1.1.4.0"),
				new ObjectIdentifier("1.3.6.1.2.1.1.5.0"),
				new ObjectIdentifier("1.3.6.1.6.3.10.2.1.1.0")));
		System.out.println(snmp.getBulk(new ObjectIdentifier("1.3.6.1.2.1.1")));
		System.out.println();
		
		List<SNMPEntry[]> table;
		table = snmp.walkBulkTable(
				new ObjectIdentifier("1.3.6.1.2.1.2.2.1.1"),
				new ObjectIdentifier("1.3.6.1.2.1.31.1.1.1.1"),
				new ObjectIdentifier("1.3.6.1.2.1.31.1.1.1.1"),
				new ObjectIdentifier("1.3.6.1.2.1.2.2.1.6"),
				new ObjectIdentifier("1.3.6.1.2.1.2.2.1.8"));
		printTable(table);
	}
	
	public static void printTable(List<SNMPEntry[]> table) {
		for (SNMPEntry[] row : table) {
			System.out.println(Arrays.toString(row));
		}
	}
	
}