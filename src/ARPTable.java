import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import at.andiwand.library.network.mac.MACAddress;


public class ARPTable {
	
	public static class ARPEntry {
		private MACAddress macAddress;
		private InetAddress inetAddress;
		
		public String toString() {
			return inetAddress.getHostAddress() + " -> " + macAddress;
		}
		
		public MACAddress getMacAddress() {
			return macAddress;
		}
		public InetAddress getInetAddress() {
			return inetAddress;
		}
		
		public void setMacAddress(MACAddress macAddress) {
			this.macAddress = macAddress;
		}
		public void setInetAddress(InetAddress inetAddress) {
			this.inetAddress = inetAddress;
		}
	}
	
	
	
	
	private List<ARPEntry> table = new ArrayList<ARPEntry>();
	
	
	
	public String toString() {
		return table.toString();
	}
	
	
	public void addEntry(ARPEntry entry) {
		table.add(entry);
	}
	
	public void removeEntry(ARPEntry entry) {
		table.remove(entry);
	}
}