import java.net.InetAddress;


public class Prototype1 {
	
	public static void main(String[] args) throws Throwable {
		InetAddress address = InetAddress.getByName("192.168.0.2");
		int port = 161;
		SNMPDeviceManager deviceManager = new SNMPDeviceManager(address, port);
		
		System.out.println(deviceManager.fetchSystemDescription());
		System.out.println(deviceManager.fetchSystemObjectId());
		System.out.println(deviceManager.fetchSystemUpTime());
		System.out.println(deviceManager.fetchSystemServices());
		System.out.println(deviceManager.fetchInterfaces());
		System.out.println(deviceManager.fetchIpForwarding());
		System.out.println(deviceManager.fetchArpTable());
	}
	
}