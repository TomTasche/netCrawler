package at.netcrawler.network.model;


public interface NetworkModelListener {
	
	public void valueChanged(String key, Object value);
	
	public void extensionAdded(NetworkModelExtension extension);
	public void extensionRemoved(NetworkModelExtension extension);
	
}