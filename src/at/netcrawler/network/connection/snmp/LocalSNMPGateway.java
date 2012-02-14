package at.netcrawler.network.connection.snmp;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public class LocalSNMPGateway extends SNMPGateway {
	
	@Override
	protected LocalSNMPConnection openConnectionGenericImpl(
			IPDeviceAccessor accessor, SNMPSettings settings)
			throws IOException {
		return new LocalSNMPConnection(accessor, settings);
	}
	
}