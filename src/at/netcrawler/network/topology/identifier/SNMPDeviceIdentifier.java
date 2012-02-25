package at.netcrawler.network.topology.identifier;

public class SNMPDeviceIdentifier extends DeviceIdentifier {
	
	private final String processorBoardId;
	
	public SNMPDeviceIdentifier(String processorBoardId) {
		this.processorBoardId = processorBoardId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof SNMPDeviceIdentifier)) return false;
		SNMPDeviceIdentifier identifier = (SNMPDeviceIdentifier) obj;
		
		return processorBoardId.equals(identifier.processorBoardId);
	}
	
	@Override
	public int hashCode() {
		return processorBoardId.hashCode();
	}
	
}