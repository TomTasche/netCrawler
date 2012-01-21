package at.netcrawler.network.topology;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class HashNetworkTopology extends NetworkTopology {
	
	private class InterfaceAdapter extends TopologyDeviceAdapter {
		@Override
		public void interfaceAdded(TopologyInterface interfaze) {
			synchronized (HashNetworkTopology.this) {
				interfaces.add(interfaze);
			}
		}
		
		@Override
		public void interfaceRemoved(TopologyInterface interfaze) {
			synchronized (HashNetworkTopology.this) {
				TopologyCable cable = connectionMap.get(interfaze);
				removeCable(cable);
				
				interfaces.remove(interfaze);
			}
		}
	}
	
	private final Set<TopologyDevice> devices = new HashSet<TopologyDevice>();
	private final Set<TopologyInterface> interfaces = new HashSet<TopologyInterface>();
	private final Map<TopologyInterface, TopologyCable> connectionMap = new HashMap<TopologyInterface, TopologyCable>();
	private final Set<TopologyCable> cables = new HashSet<TopologyCable>();
	
	public HashNetworkTopology() {}
	
	public HashNetworkTopology(NetworkTopology networkTopology) {
		for (TopologyDevice device : networkTopology.getDevices()) {
			addDevice(device);
		}
		
		for (TopologyCable cable : networkTopology.getCables()) {
			addCable(cable);
		}
	}
	
	@Override
	public synchronized int getDeviceCount() {
		return devices.size();
	}
	
	@Override
	public synchronized int getCableCount() {
		return cables.size();
	}
	
	@Override
	public synchronized Set<TopologyDevice> getDevices() {
		return new HashSet<TopologyDevice>(devices);
	}
	
	@Override
	public synchronized Set<TopologyCable> getCables() {
		return new HashSet<TopologyCable>(cables);
	}
	
	@Override
	public synchronized Map<TopologyInterface, TopologyCable> getConnectionMap() {
		return new HashMap<TopologyInterface, TopologyCable>(connectionMap);
	}
	
	@Override
	public synchronized Set<TopologyCable> getConnectedCables(
			TopologyDevice device) {
		Set<TopologyCable> result = new HashSet<TopologyCable>();
		
		for (TopologyInterface interfaze : device.getInterfaces()) {
			TopologyCable cable = connectionMap.get(interfaze);
			result.add(cable);
		}
		
		return result;
	}
	
	@Override
	public synchronized boolean addDevice(TopologyDevice device) {
		if (devices.contains(device)) return false;
		
		device.addListener(new InterfaceAdapter());
		
		for (TopologyInterface interfaze : device.getInterfaces()) {
			interfaces.add(interfaze);
		}
		
		devices.add(device);
		
		return true;
	}
	
	@Override
	public synchronized boolean addCable(TopologyCable cable) {
		if (cables.contains(cable)) return false;
		
		for (TopologyInterface interfaze : cable.getConnectedInterfaces()) {
			if (!interfaces.contains(interfaze)) return false;
			if (connectionMap.containsKey(interfaze)) return false;
			connectionMap.put(interfaze, cable);
		}
		
		cables.add(cable);
		
		return true;
	}
	
	@Override
	public synchronized boolean removeDevice(TopologyDevice device) {
		if (!devices.contains(device)) return false;
		
		for (TopologyInterface interfaze : device.getInterfaces()) {
			TopologyCable cable = connectionMap.get(interfaze);
			removeCable(cable);
			
			interfaces.remove(interfaze);
		}
		
		devices.remove(device);
		
		return true;
	}
	
	@Override
	public synchronized boolean removeCable(TopologyCable cable) {
		if (!cables.contains(cable)) return false;
		
		for (TopologyInterface interfaze : cable.getConnectedInterfaces()) {
			connectionMap.remove(interfaze);
		}
		
		cables.remove(cable);
		
		return true;
	}
	
	@Override
	public synchronized boolean removeAllCables(TopologyCable cable) {
		return super.removeAllCables(cable);
	}
	
}