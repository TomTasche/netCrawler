package at.netcrawler.network.connection;

public abstract class ConnectionSettings {
	
	public static final int DEFAULT_TIMEOUT = 1500;
	
	private int timeout;
	
	public ConnectionSettings() {
		timeout = DEFAULT_TIMEOUT;
	}
	
	public ConnectionSettings(ConnectionSettings settings) {
		timeout = settings.timeout;
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
}