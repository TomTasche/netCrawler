package at.netcrawler.network.connection;

public abstract class TCPIPConnectionSettings extends ConnectionSettings {
	
	private int port;
	
	public TCPIPConnectionSettings() {}
	
	public TCPIPConnectionSettings(TCPIPConnectionSettings settings) {
		setPort(settings.port);
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
}