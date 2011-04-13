package at.rennweg.htl.netcrawler.network.ssh;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import at.rennweg.htl.netcrawler.cli.executor.SimpleRemoteExecutor;


public abstract class SSHExecutor implements SimpleRemoteExecutor {
	
	public static final int DEFAULT_PORT = 22;
	
	
	
	public SSHExecutor() {}
	public SSHExecutor(String login, String password) throws Exception {
		connect(login, password);
	}
	public SSHExecutor(String host, String username, String password) throws Exception {
		connect(host, username, password);
	}
	public SSHExecutor(InetAddress address, String username, String password) throws Exception {
		connect(address, username, password);
	}
	public SSHExecutor(InetSocketAddress socketAddress, String username, String password) throws Exception {
		connect(socketAddress, username, password);
	}
	public SSHExecutor(String host, int port, String username, String password) throws Exception {
		connect(host, port, username, password);
	}
	public SSHExecutor(InetAddress address, int port, String username, String password) throws Exception {
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
	
	public abstract void close();
	
	
	@Override
	public abstract String execute(String command) throws IOException;
	
	public abstract int lastExitStatus();
	
}