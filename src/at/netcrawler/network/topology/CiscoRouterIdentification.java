package at.netcrawler.network.topology;

public class CiscoRouterIdentification extends DeviceIdentification {
	
	private final String processorBoardId;
	
	public CiscoRouterIdentification(String processorBoardId) {
		this.processorBoardId = processorBoardId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof CiscoRouterIdentification)) return false;
		CiscoRouterIdentification identification = (CiscoRouterIdentification) obj;
		
		return processorBoardId.equals(identification.processorBoardId);
	}
	
	@Override
	public int hashCode() {
		return processorBoardId.hashCode();
	}
	
}