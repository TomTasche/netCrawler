package at.netcrawler.network.connection.snmp;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.TCPIPConnectionGateway;
import at.netcrawler.network.connection.TCPIPDeviceConnection;


public abstract class SNMPGateway extends TCPIPConnectionGateway<SNMPSettings> {
	
	@Override
	public abstract TCPIPDeviceConnection openConnection(
			IPDeviceAccessor accessor, SNMPSettings settings)
			throws IOException;
	
}