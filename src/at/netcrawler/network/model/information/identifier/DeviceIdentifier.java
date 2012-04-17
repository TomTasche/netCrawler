package at.netcrawler.network.model.information.identifier;

public abstract class DeviceIdentifier {
	
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();
	
}