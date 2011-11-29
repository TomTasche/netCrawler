package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public abstract class IPConnectionGateway<CS extends ConnectionSettings>
		extends ConnectionGateway<IPDeviceAccessor, CS> {
	
	@Override
	public Class<IPDeviceAccessor> getDeviceAccessorClass() {
		return IPDeviceAccessor.class;
	}
	
	public abstract IPDeviceConnection openConnection(
			IPDeviceAccessor accessor, CS settings) throws IOException;
	
}