package at.netcrawler;

public enum DeviceSystem {
	
	LINUX("Linux"), MICROSOFT("Microsoft Corporation"), CISCO(
			"Cisco Systems, Inc.");
	
	private final String company;
	
	private DeviceSystem(String company) {
		this.company = company;
	}
	
	@Override
	public String toString() {
		return company;
	}
	
	public String getCompany() {
		return company;
	}
	
}