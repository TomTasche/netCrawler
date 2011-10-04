import at.andiwand.library.network.mac.MACAddress;

public class Interface {
	
	private int number;
	private String description;
	private int mtu;
	private MACAddress macAddress;
	private int adminState;
	private String name;
	
	
	
	public String toString() {
		return number + ": " + name + ", " + macAddress + " - " + description
				+ "; state: " + adminState;
	}
	
	
	public int getNumber() {
		return number;
	}
	public String getDescription() {
		return description;
	}
	public int getMtu() {
		return mtu;
	}
	public MACAddress getMacAddress() {
		return macAddress;
	}
	public int getAdminState() {
		return adminState;
	}
	public String getName() {
		return name;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setMtu(int mtu) {
		this.mtu = mtu;
	}
	public void setMacAddress(MACAddress macAddress) {
		this.macAddress = macAddress;
	}
	public void setAdminState(int adminState) {
		this.adminState = adminState;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}