package at.netcrawler.network.connection.ssh.console.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.console.LocalSSHConsoleConnection;
import at.netcrawler.network.connection.ssh.console.SSHConsoleConnectionSettings;


public abstract class LocalSSHConsoleConnectionImpl extends
		LocalSSHConsoleConnection {
	
	public static LocalSSHConsoleConnectionImpl getInstance(
			IPDeviceAccessor accessor, SSHConsoleConnectionSettings settings)
			throws IOException {
		switch (settings.getVersion()) {
		case VERSION1:
			return new LocalSSH1ConsoleConnectionImpl(accessor, settings);
		case VERSION2:
			return new LocalSSH2ConsoleConnectionImpl(accessor, settings);
		}
		
		throw new IllegalStateException("Unreachable section");
	}
	
	public LocalSSHConsoleConnectionImpl(IPDeviceAccessor accessor,
			SSHConsoleConnectionSettings settings) throws IOException {
		super(accessor, settings);
	}
	
	@Override
	public abstract InputStream getInputStream() throws IOException;
	
	@Override
	public abstract OutputStream getOutputStream() throws IOException;
	
	@Override
	protected abstract void closeImpl() throws IOException;
	
}