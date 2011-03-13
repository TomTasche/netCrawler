package at.andiwand.library.network.ssh;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import at.andiwand.library.util.cli.CommandLine;



public abstract class SSHClient implements CommandLine {
	
	public static final int DEFAULT_PORT = 22;
	
	
	
	public SSHClient() {}
	public SSHClient(String login, String password) throws Exception {
		connect(login, password);
	}
	public SSHClient(String host, String username, String password) throws Exception {
		connect(host, username, password);
	}
	public SSHClient(InetAddress address, String username, String password) throws Exception {
		connect(address, username, password);
	}
	public SSHClient(InetSocketAddress socketAddress, String username, String password) throws Exception {
		connect(socketAddress, username, password);
	}
	public SSHClient(String host, int port, String username, String password) throws Exception {
		connect(host, port, username, password);
	}
	public SSHClient(InetAddress address, int port, String username, String password) throws Exception {
		connect(address, port, username, password);
	}
	
	
	public void connect(String login, String password) throws Exception {
		connect(login.split("@")[1], DEFAULT_PORT, login.split("@")[0], password);
	}
	public void connect(String host, String username, String password) throws Exception {
		connect(host, DEFAULT_PORT, username, password);
	}
	public void connect(InetAddress address, String username, String password) throws Exception {
		connect(address, DEFAULT_PORT, username, password);
	}
	public void connect(InetSocketAddress socketAddress, String username, String password) throws Exception {
		connect(socketAddress.getAddress(), socketAddress.getPort(), username, password);
	}
	public void connect(String host, int port, String username, String password) throws Exception {
		connect(InetAddress.getByName(host), port, username, password);
	}
	public abstract void connect(InetAddress address, int port, String username, String password) throws Exception;
	
}