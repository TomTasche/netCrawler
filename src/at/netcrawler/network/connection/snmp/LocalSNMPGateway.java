package at.netcrawler.network.connection.snmp;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.TCPIPDeviceConnection;


public class LocalSNMPGateway extends SNMPGateway {
	
	@Override
	public TCPIPDeviceConnection openConnection(IPDeviceAccessor accessor,
			SNMPSettings settings) throws IOException {
		return new LocalSNMPConnection(accessor, settings);
	}
	
}