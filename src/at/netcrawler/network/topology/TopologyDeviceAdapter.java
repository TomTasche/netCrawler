package at.netcrawler.network.topology;

public abstract class TopologyDeviceAdapter implements TopologyDeviceListener {
	
	@Override
	public abstract void interfaceAdded(TopologyInterface interfaze);
	
	@Override
	public abstract void interfaceRemoved(TopologyInterface interfaze);
	
}