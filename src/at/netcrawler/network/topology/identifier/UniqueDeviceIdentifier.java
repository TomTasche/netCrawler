package at.netcrawler.network.topology.identifier;

public class UniqueDeviceIdentifier extends DeviceIdentifier {
	
	private final Object identifier = new Object();
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof UniqueDeviceIdentifier)) return false;
		UniqueDeviceIdentifier identification = (UniqueDeviceIdentifier) obj;
		
		return identifier.equals(identification.identifier);
	}
	
	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
	
}