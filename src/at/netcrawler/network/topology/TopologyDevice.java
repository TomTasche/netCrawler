package at.netcrawler.network.topology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.information.identifier.DeviceIdentifier;
import at.netcrawler.network.model.information.identifier.DeviceIdentifierBuilder;


public class TopologyDevice {
	
	private static final DeviceIdentifierBuilder IDENTIFIER_BUILDER = DeviceIdentifierBuilder
			.getDefaultBuilder();
	
	private final DeviceIdentifier identifier;
	private final NetworkDevice networkDevice;
	// TODO: hotfix
	@Deprecated
	private final DeviceManager deviceManager;
	private Set<TopologyInterface> interfaces = new HashSet<TopologyInterface>();
	
	private List<TopologyDeviceListener> listeners = new ArrayList<TopologyDeviceListener>();
	
	public TopologyDevice(NetworkDevice networkDevice) {
		this(IDENTIFIER_BUILDER.getIdentification(networkDevice), networkDevice);
	}
	
	public TopologyDevice(DeviceIdentifier identifier,
			NetworkDevice networkDevice) {
		this(identifier, networkDevice, null);
	}
	
	// TODO: hotfix
	@Deprecated
	public TopologyDevice(DeviceIdentifier identifier,
			NetworkDevice networkDevice, DeviceManager deviceManager) {
		this.identifier = identifier;
		this.networkDevice = networkDevice;
		this.deviceManager = deviceManager;
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
	
	// TODO: hotfix
	@Deprecated
	public DeviceManager getDeviceManager() {
		return deviceManager;
	}
	
	public String getHostname() {
		return (String) networkDevice.getValue(NetworkDevice.HOSTNAME);
	}
	
	public Set<TopologyInterface> getInterfaces() {
		synchronized (interfaces) {
			return new HashSet<TopologyInterface>(interfaces);
		}
	}
	
	public TopologyInterface getInterfaceByName(String name) {
		for (TopologyInterface interfaze : interfaces) {
			if (interfaze.getName().equals(name)) return interfaze;
		}
		
		return null;
	}
	
	public boolean addInterface(TopologyInterface interfaze) {
		synchronized (interfaces) {
			if (interfaces.contains(interfaze)) return false;
			
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
			if (!interfaces.contains(interfaze)) return false;
			
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