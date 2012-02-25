package at.netcrawler.network.topology.identifier;

public abstract class DeviceIdentifier {
	
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();
	
}