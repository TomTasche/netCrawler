package at.netcrawler.network.connection.snmp;

public enum SNMPVersion {
	
	VERSION1,
	VERSION2C,
	VERSION3;
	
	public String toString() {
		String s = super.toString();
		return s.replace("VERSION", "v");
	}
}