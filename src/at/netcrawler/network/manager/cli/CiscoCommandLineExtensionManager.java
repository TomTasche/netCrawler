package at.netcrawler.network.manager.cli;

import java.io.IOException;

import at.netcrawler.network.manager.CiscoExtensionManager;


public class CiscoCommandLineExtensionManager extends CiscoExtensionManager {
	
	@Override
	public String getModelNumber() throws IOException {
		return null;
	}
	
	@Override
	public String getSystemSerialNumber() throws IOException {
		return null;
	}
	
	@Override
	public String getProcessorString() throws IOException {
		return null;
	}
	
}