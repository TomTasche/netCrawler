package at.netcrawler.network.topology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.topology.identifier.DeviceIdentifier;


public class TopologyDevice {
	
	private final DeviceIdentifier identifier;
	private final NetworkDevice networkDevice;
	private Set<TopologyInterface> interfaces = new HashSet<TopologyInterface>();
	
	private List<TopologyDeviceListener> listeners = new ArrayList<TopologyDeviceListener>();
	
	public TopologyDevice(DeviceIdentifier identifier,
			NetworkDevice networkDevice) {
		this.identifier = identifier;
		this.networkDevice = networkDevice;
	}
	
	@Override
	public String toString() {
		return getHostname();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof TopologyDevice)) return false;
		TopologyDevice device = (TopologyDevice) obj;
		
		return identifier.equals(device.identifier);
	}
	
	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
	
	public NetworkDevice getNetworkDevice() {
		return networkDevice;
	}
	
	public String getHostname() {
		return (String) networkDevice.getValue(NetworkDevice.HOSTNAME);
	}
	
	public Set<TopologyInterface> getInterfaces() {
		synchronized (interfaces) {
			return new HashSet<TopologyInterface>(interfaces);
		}
	}
	
	public boolean addInterface(TopologyInterface interfaze) {
		synchronized (interfaces) {
			if (!interfaces.contains(interfaze)) return false;
			
			if (interfaze.getDevice() != null)
				interfaze.getDevice().removeInterface(interfaze);
			
			interfaze.setDevice(this);
			interfaces.add(interfaze);
			
			fireInterfaceAdded(interfaze);
			return true;
		}
	}
	
	public void addListener(TopologyDeviceListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	public boolean removeInterface(TopologyInterface interfaze) {
		synchronized (interfaces) {
			if (interfaces.contains(interfaze)) return false;
			
			interfaze.setDevice(null);
			interfaces.remove(interfaze);
			
			fireInterfaceRemoved(interfaze);
			return true;
		}
	}
	
	public void removeListener(TopologyDeviceListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	private void fireInterfaceAdded(TopologyInterface interfaze) {
		synchronized (listeners) {
			for (TopologyDeviceListener listener : listeners) {
				listener.interfaceAdded(interfaze);
			}
		}
	}
	
	private void fireInterfaceRemoved(TopologyInterface interfaze) {
		synchronized (listeners) {
			for (TopologyDeviceListener listener : listeners) {
				listener.interfaceRemoved(interfaze);
			}
		}
	}
	
}