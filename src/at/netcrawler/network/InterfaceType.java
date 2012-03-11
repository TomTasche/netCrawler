package at.netcrawler.network;

public enum InterfaceType {
	
	// TODO: complete
	LOOPBACK,
	ETHERNET(CableType.ETHERNET),
	FRAME_RELAY(CableType.SERIAL),
	UNKNOWN(CableType.UNKNOWN);
	
	private final CableType suitableCable;
	
	private InterfaceType() {
		this(null);
	}
	
	private InterfaceType(CableType suitableCable) {
		this.suitableCable = suitableCable;
	}
	
	public CableType getSuitableCable() {
		return suitableCable;
	}
	
	public boolean suitsCable(CableType cableType) {
		return suitableCable == cableType;
	}
	
}