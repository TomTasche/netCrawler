package at.netcrawler.network.topology.identifier;

public class CiscoSwitchIdentifier extends DeviceIdentifier {
	
	private final String systemSerialNumber;
	
	public CiscoSwitchIdentifier(String systemSerialNumber) {
		this.systemSerialNumber = systemSerialNumber;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof CiscoSwitchIdentifier)) return false;
		CiscoSwitchIdentifier identifier = (CiscoSwitchIdentifier) obj;
		
		return systemSerialNumber.equals(identifier.systemSerialNumber);
	}
	
	@Override
	public int hashCode() {
		return systemSerialNumber.hashCode();
	}
	
}