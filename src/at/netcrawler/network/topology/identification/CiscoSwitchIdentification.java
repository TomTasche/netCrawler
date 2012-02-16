package at.netcrawler.network.topology.identification;

public class CiscoSwitchIdentification extends DeviceIdentification {
	
	private final String systemSerialNumber;
	
	public CiscoSwitchIdentification(String systemSerialNumber) {
		this.systemSerialNumber = systemSerialNumber;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof CiscoSwitchIdentification)) return false;
		CiscoSwitchIdentification identification = (CiscoSwitchIdentification) obj;
		
		return systemSerialNumber.equals(identification.systemSerialNumber);
	}
	
	@Override
	public int hashCode() {
		return systemSerialNumber.hashCode();
	}
	
}