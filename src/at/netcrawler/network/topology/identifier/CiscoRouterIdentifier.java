package at.netcrawler.network.topology.identifier;

public class CiscoRouterIdentifier extends DeviceIdentifier {
	
	private final String processorBoardId;
	
	public CiscoRouterIdentifier(String processorBoardId) {
		this.processorBoardId = processorBoardId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof CiscoRouterIdentifier)) return false;
		CiscoRouterIdentifier identifier = (CiscoRouterIdentifier) obj;
		
		return processorBoardId.equals(identifier.processorBoardId);
	}
	
	@Override
	public int hashCode() {
		return processorBoardId.hashCode();
	}
	
}