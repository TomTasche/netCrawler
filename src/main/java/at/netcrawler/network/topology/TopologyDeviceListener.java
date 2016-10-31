package at.netcrawler.network.topology;

public interface TopologyDeviceListener {
	
	public void interfaceAdded(TopologyInterface interfaze);
	
	public void interfaceRemoved(TopologyInterface interfaze);
	
}