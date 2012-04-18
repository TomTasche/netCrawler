package at.netcrawler.network.connection.snmp;

public enum SNMPVersion {
	
	VERSION_1,
	VERSION_2C,
	VERSION_3;
	
	public String toString() {
		String s = super.toString();
		return s.replace("VERSION_", "v");
	}
}