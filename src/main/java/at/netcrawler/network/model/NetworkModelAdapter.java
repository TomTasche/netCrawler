package at.netcrawler.network.model;

public abstract class NetworkModelAdapter implements NetworkModelListener {
	
	public void valueChanged(String key, Object value, Object oldValue) {}
	
	public void extensionAdded(NetworkModelExtension extension) {}
	
	public void extensionRemoved(NetworkModelExtension extension) {}
	
}