package at.netcrawler.network.accessor;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

import at.andiwand.library.network.ip.IPAddress;
import at.andiwand.library.network.ip.IPv4Address;


public class IPDeviceAccessor extends DeviceAccessor {
	
	private final IPAddress ipAddress;
	
	public IPDeviceAccessor(IPAddress ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public IPDeviceAccessor(InetAddress inetAddress) {
		if (inetAddress instanceof Inet4Address) {
			ipAddress = new IPv4Address(inetAddress);
		} else if (inetAddress instanceof Inet6Address) {
			throw new UnsupportedOperationException(
					"Inet6Address is not supported");
		} else {
			throw new IllegalArgumentException("Illegal argument");
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof IPDeviceAccessor)) return false;
		IPDeviceAccessor accessor = (IPDeviceAccessor) obj;
		
		return ipAddress.equals(accessor.ipAddress);
	}
	
	@Override
	public int hashCode() {
		return ipAddress.hashCode();
	}
	
	public IPAddress getIpAddress() {
		return ipAddress;
	}
	
	public InetAddress getInetAddress() {
		return ipAddress.toInetAddress();
	}
	
}