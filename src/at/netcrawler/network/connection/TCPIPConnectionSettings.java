package at.netcrawler.network.connection;

public abstract class TCPIPConnectionSettings extends ConnectionSettings {
	
	private int port;
	
	public TCPIPConnectionSettings() {}
	
	public TCPIPConnectionSettings(TCPIPConnectionSettings settings) {
		super(settings);
		
		setPort(settings.port);
	}
	
	@Override
	public abstract TCPIPConnectionSettings clone();
	
	@Override
	public abstract Class<? extends TCPIPDeviceConnection> getConnectionClass();
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
}