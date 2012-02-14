package at.netcrawler.network.model;

public abstract class NetworkModelAdapter implements NetworkModelListener {
	
	@Override
	public void valueChanged(String key, Object value, Object oldValue) {}
	
	@Override
	public void extensionAdded(NetworkModelExtension extension) {}
	
	@Override
	public void extensionRemoved(NetworkModelExtension extension) {}
	
}