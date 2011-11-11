package at.netcrawler.network.connection.telnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import at.netcrawler.network.IPDeviceAccessor;


public class LocalTelnetConnection extends TelnetConnection {
	
	private Socket socket;
	
	
	public LocalTelnetConnection(IPDeviceAccessor accessor,
			TelnetConnectionSettings settings) throws IOException {
		super(accessor, settings);
		
		InetSocketAddress endpoint = new InetSocketAddress(
				accessor.getInetAddress(), settings.getPort());
		
		socket = new Socket();
		socket.connect(endpoint, settings.getTimeout());
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}
	
	@Override
	protected void closeImpl() throws IOException {
		socket.close();
	}
	
}