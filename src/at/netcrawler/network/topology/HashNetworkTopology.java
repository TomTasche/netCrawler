package at.netcrawler.network.topology;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class HashNetworkTopology extends NetworkTopology {
	
	private final Set<TopologyDevice> devices;
	private final Map<TopologyInterface, TopologyCable> connectionMap;
	private final Set<TopologyCable> cables;
	
	public HashNetworkTopology() {
		devices = new HashSet<TopologyDevice>();
		connectionMap = new HashMap<TopologyInterface, TopologyCable>();
		cables = new HashSet<TopologyCable>();
	}
	
	public HashNetworkTopology(NetworkTopology networkTopology) {
		devices = new HashSet<TopologyDevice>(networkTopology.getDevices());
		cables = new HashSet<TopologyCable>(networkTopology.getCables());
		
		connectionMap = new HashMap<TopologyInterface, TopologyCable>();
		
		for (TopologyCable cable : cables) {
			for (TopologyInterface interfaze : cable.getConnectedInterfaces()) {
				if (connectionMap.containsKey(interfaze)) throw new IllegalArgumentException(
						"Illegal cable!");
				connectionMap.put(interfaze, cable);
			}
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
	public synchronized boolean addDevice(TopologyDevice device) {
		return devices.add(device);
	}
	
	@Override
	public synchronized boolean addCable(TopologyCable cable) {
		if (cables.contains(cable)) return false;
		
		for (TopologyInterface interfaze : cable.getConnectedInterfaces()) {
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