package at.netcrawler.network.model.information.identifier;

import at.netcrawler.network.model.information.ModelInformation;


public abstract class DeviceIdentifier extends ModelInformation {
	
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();
	
}