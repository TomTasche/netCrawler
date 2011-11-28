package at.netcrawler.network;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import at.andiwand.library.network.mac.MACAddress;


public class ARPTable implements Iterable<ARPTable.Entry> {
	
	public static class Entry {
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
	
	private List<Entry> table = new ArrayList<Entry>();
	
	public String toString() {
		return table.toString();
	}
	
	public List<Entry> getTable() {
		return new ArrayList<Entry>(table);
	}
	
	public void addEntry(Entry entry) {
		table.add(entry);
	}
	
	public void removeEntry(Entry entry) {
		table.remove(entry);
	}
	
	@Override
	public Iterator<Entry> iterator() {
		return table.iterator();
	}
}