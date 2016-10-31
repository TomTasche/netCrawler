package at.netcrawler.ui.assistant;

import java.net.InetAddress;
import java.util.LinkedHashMap;
import java.util.Set;


// TODO: accessor, connection class, connection settings
public class Configuration {
	
	public static final String FILE_SUFFIX = ".conf";
	
	private Set<InetAddress> addresses;
	private ConnectionContainer connection;
	private int port;
	private String username;
	private String password;
	private LinkedHashMap<String, String> batches = new LinkedHashMap<String, String>();
	
	public Set<InetAddress> getAddresses() {
		return addresses;
	}
	
	public ConnectionContainer getConnectionContainer() {
		return connection;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public LinkedHashMap<String, String> getBatches() {
		return new LinkedHashMap<String, String>(batches);
	}
	
	public String getBatch(String name) {
		return batches.get(name);
	}
	
	public void setAddresses(Set<InetAddress> addresses) {
		this.addresses = addresses;
	}
	
	public void setConnection(ConnectionContainer connection) {
		this.connection = connection;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setBatches(LinkedHashMap<String, String> batches) {
		this.batches = new LinkedHashMap<String, String>(batches);
	}
	
	public void putBatch(String name, String batch) {
		batches.put(name, batch);
	}
	
	public void removeBatch(String name) {
		batches.remove(name);
	}
}