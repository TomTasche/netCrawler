package at.netcrawler.network.topology;

public class SNMPDeviceIdentification extends DeviceIdentification {
	
	private final String processorBoardId;
	
	public SNMPDeviceIdentification(String processorBoardId) {
		this.processorBoardId = processorBoardId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof SNMPDeviceIdentification)) return false;
		SNMPDeviceIdentification identification = (SNMPDeviceIdentification) obj;
		
		return processorBoardId.equals(identification.processorBoardId);
	}
	
	@Override
	public int hashCode() {
		return processorBoardId.hashCode();
	}
	
}