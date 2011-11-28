package at.netcrawler.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.network.IPDeviceAccessor;
import at.netcrawler.network.connection.snmp.LocalSNMPConnection;
import at.netcrawler.network.connection.snmp.SNMPConnectionSettings;
import at.netcrawler.network.connection.snmp.SNMPSecurityLevel;
import at.netcrawler.network.connection.snmp.SNMPVersion;

public class OidFetcher {
	
	@SuppressWarnings("serial")
	public static void main(String[] args) throws IOException {
		Map<String, String> namesForOid = new HashMap<String, String>() {
			{
				put("1.3.6.1.2.1.1.1", "os"); // Linux beathe 3.0.0-13-generic #22-Ubuntu SMP Wed Nov 2 13:27:26 UTC 2011 x86_64
				put("1.3.6.1.2.1.31.1.1.1.1", "interface.name");
				put("1.3.6.1.2.1.2.2.1.2", "interface.description");
				put("1.3.6.1.2.1.2.2.1.6", "interface.address");
				put("1.3.6.1.2.1.1.3", "uptime"); // 0:11:09.37
				put("1.3.6.1.2.1.1.5", "hostname"); // beathe
				put("1.3.6.1.4.1.9.2.10.17", "flash");
				put("1.3.6.1.2.1.4.22.1.2", "arp");
				put("1.3.6.1.2.1.1.7", "type"); // 72
				put("1.3.6.1.4.1.437.1.1.3.6.3", "vlan");
				put("1.3.6.1.4.1.9.9.112.1.5.2.1.4", "fr");
				put("1.3.6.1.4.1.9.9.227.1.2.1", "acl");
				put("1.3.6.1.4.1.255.2.2.1.1", "vpn");
				put("1.3.6.1.2.1.4.21", "routing");
				put("1.3.6.1.4.1.351.110.2.1.22", "cam");
				put("1.3.6.1.4.1.9.9.23.1.2.1", "cdp");
				put("1.3.6.1.2.1.47.1.1.1.1.11", "serial");
			}
		};
		
		String address = "127.0.0.1";
//		String address = "192.168.1.5";
		IPv4Address ipAddress = IPv4Address.getByAddress(address);
		int port = 161;
		
		
		IPDeviceAccessor accessor = new IPDeviceAccessor(ipAddress);
		
		SNMPConnectionSettings settings = new SNMPConnectionSettings();
		settings.setPort(port);
		settings.setVersion(SNMPVersion.VERSION2C);
		settings.setCommunity("netCrawler");
		settings.setSecurityLevel(SNMPSecurityLevel.NOAUTH_NOPRIV);
		
		LocalSNMPConnection connection = new LocalSNMPConnection(accessor,
				settings);
		
		for (String oid : namesForOid.keySet()) {
//			String result = connection.get(oid).getValue();
			String result = connection.walk(oid).toString();
//			String result = connection.walkTable(oid).toString();
//			String result = connection.getBulk(oid).toString();
			System.out.println(namesForOid.get(oid) + ":");
			System.out.println(result);
			System.out.println("----");
		}
	}
}
