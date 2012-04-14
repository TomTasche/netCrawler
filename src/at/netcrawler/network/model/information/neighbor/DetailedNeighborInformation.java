package at.netcrawler.network.model.information.neighbor;

import java.util.Set;

import at.netcrawler.network.model.Capability;
import at.netcrawler.network.model.DeviceSystem;


public interface DetailedNeighborInformation extends NeighborInformation {
	
	public String getHostname();
	
	public DeviceSystem getSystem();
	
	public String getSystemDescription();
	
	public Set<Capability> getCapabilities();
	
	public String getLocalInterface();
	
	public String getRemoteInterface();
	
}