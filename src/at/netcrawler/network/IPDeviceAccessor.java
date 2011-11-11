package at.netcrawler.network;

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
			ipAddress = IPv4Address.getByAddress(inetAddress);
		} else if (inetAddress instanceof Inet6Address) {
			throw new UnsupportedOperationException(
					"Inet6Address is not supported");
		} else {
			throw new IllegalArgumentException("Illegal argument");
		}
	}
	
	
	public IPAddress getIpAddress() {
		return ipAddress;
	}
	public InetAddress getInetAddress() {
		return ipAddress.toInetAddress();
	}
	
}