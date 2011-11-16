package at.netcrawler.network.connection.ssh.deprecated;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import at.andiwand.library.cli.CommandLine;


public abstract class D_SSHClient implements CommandLine {
	
	public static final int DEFAULT_PORT = 22;
	
	
	
	public D_SSHClient() {}
	public D_SSHClient(String login, String password) throws IOException {
		connect(login, password);
	}
	public D_SSHClient(String host, String username, String password) throws IOException {
		connect(host, username, password);
	}
	public D_SSHClient(InetAddress address, String username, String password) throws IOException {
		connect(address, username, password);
	}
	public D_SSHClient(InetSocketAddress socketAddress, String username, String password) throws IOException {
		connect(socketAddress, username, password);
	}
	public D_SSHClient(String host, int port, String username, String password) throws IOException {
		connect(host, port, username, password);
	}
	public D_SSHClient(InetAddress address, int port, String username, String password) throws IOException {
		connect(address, port, username, password);
	}
	
	
	public void connect(String login, String password) throws IOException {
		connect(login.split("@")[1], DEFAULT_PORT, login.split("@")[0], password);
	}
	public void connect(String host, String username, String password) throws IOException {
		connect(host, DEFAULT_PORT, username, password);
	}
	public void connect(InetAddress address, String username, String password) throws IOException {
		connect(address, DEFAULT_PORT, username, password);
	}
	public void connect(InetSocketAddress socketAddress, String username, String password) throws IOException {
		connect(socketAddress.getAddress(), socketAddress.getPort(), username, password);
	}
	public void connect(String host, int port, String username, String password) throws IOException {
		connect(InetAddress.getByName(host), port, username, password);
	}
	public abstract void connect(InetAddress address, int port, String username, String password) throws IOException;
	
}