package at.rennweg.htl.netcrawler.network.agent;

import java.io.IOException;

import at.andiwand.library.util.SimpleRemoteExecutor;
import at.rennweg.htl.netcrawler.network.graph.CiscoDevice;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;


public abstract class SimpleCiscoDeviceAgent extends SimpleNetworkDeviceAgent {
	
	public SimpleCiscoDeviceAgent(SimpleRemoteExecutor executor) {
		super(executor);
	}
	
	
	
	public abstract String fetchSeriesNumber() throws IOException;
	
	
	@Override
	public CiscoDevice fetchAll() throws IOException {
		CiscoDevice result = new CiscoDevice();
		
		fetchAll(result);
		
		return result;
	}
	@Override
	public void fetchAll(NetworkDevice device) throws IOException {
		if (!(device instanceof CiscoDevice))
			throw new IllegalArgumentException("The device must be a CiscoDevice!");
		
		CiscoDevice ciscoDevice = (CiscoDevice) device;
		
		super.fetchAll(device);
		ciscoDevice.setSeriesNumber(fetchSeriesNumber());
	}
	
}