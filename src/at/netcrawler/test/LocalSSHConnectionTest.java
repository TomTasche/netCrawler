package at.netcrawler.test;

import java.io.InputStream;
import java.io.OutputStream;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.network.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHConnectionSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;


public class LocalSSHConnectionTest {
	
	public static void main(String[] args) throws Throwable {
		SSHVersion version = SSHVersion.VERSION2;
		String address = "192.168.0.254";
		IPv4Address ipAddress = IPv4Address.getByAddress(address);
		int port = 22;
		String username = "cisco";
		String password = "cisco";
		
		IPDeviceAccessor accessor = new IPDeviceAccessor(ipAddress);
		
		SSHConnectionSettings settings = new SSHConnectionSettings();
		settings.setVersion(version);
		settings.setPort(port);
		settings.setUsername(username);
		settings.setPassword(password);
		
		LocalSSHConnection connection = new LocalSSHConnection(accessor,
				settings);
		InputStream inputStream = connection.getInputStream();
		OutputStream outputStream = connection.getOutputStream();
		
		outputStream.write("\n".getBytes());
		
		while (true) {
			System.out.write(inputStream.read());
		}
	}
	
}