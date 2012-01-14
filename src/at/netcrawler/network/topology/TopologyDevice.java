package at.netcrawler.network.topology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.model.NetworkModelAdapter;


public class TopologyDevice {
	
	private class DeviceAdapter extends NetworkModelAdapter {
		@Override
		public void valueChanged(String key, Object value, Object oldValue) {
			if (!key.equals(NetworkDevice.INTERFACES)) return;
			
			setNetworkInterface(value);
		}
	}
	
	private final NetworkDevice networkDevice;
	
	private Set<TopologyInterface> interfaces = new HashSet<TopologyInterface>();
	
	private List<TopologyDeviceListener> listeners = new ArrayList<TopologyDeviceListener>();
	
	public TopologyDevice(NetworkDevice networkDevice) {
		this.networkDevice = networkDevice;
		
		networkDevice.addModelListener(new DeviceAdapter());
		setDeviceInterfaces();
	}
	
	@Override
	public String toString() {
		String hostname = (String) networkDevice.getValue(NetworkDevice.HOSTNAME);
		
		return hostname;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof TopologyDevice)) return false;
		TopologyDevice device = (TopologyDevice) obj;
		
		String identicationA = (String) networkDevice.getValue(NetworkDevice.IDENTICATION);
		String identicationB = (String) device.networkDevice.getValue(NetworkDevice.IDENTICATION);
		
		if (identicationA == identicationB) return true;
		if (identicationA == null) return false;
		return identicationA.equals(identicationB);
	}
	
	@Override
	public int hashCode() {
		String identication = (String) networkDevice.getValue(NetworkDevice.IDENTICATION);
		if (identication == null) return 0;
		return identication.hashCode();
	}
	
	public NetworkDevice getNetworkDevice() {
		return networkDevice;
	}
	
	public Set<TopologyInterface> getInterfaces() {
		synchronized (interfaces) {
			return new HashSet<TopologyInterface>(interfaces);
		}
	}
	
	public TopologyInterface getInterfaceByName(String name) {
		synchronized (interfaces) {
			for (TopologyInterface interfaze : interfaces) {
				if (name.equals(interfaze.getName())) return interfaze;
			}
		}
		
		return null;
	}
	
	private void setInterfaces(Set<TopologyInterface> newInterfaces) {
		Set<TopologyInterface> oldInterfaces = interfaces;
		interfaces = new HashSet<TopologyInterface>(newInterfaces);
		
		fireInterfacesChange(oldInterfaces, newInterfaces);
	}
	
	private void setNetworkInterface(Set<NetworkInterface> networkInterfaces) {
		Set<TopologyInterface> interfaces = new HashSet<TopologyInterface>();
		
		for (NetworkInterface networkInterface : networkInterfaces) {
			TopologyInterface interfaze = new TopologyInterface(
					networkInterface, this);
			interfaces.add(interfaze);
		}
		
		setInterfaces(interfaces);
	}
	
	@SuppressWarnings("unchecked")
	private void setNetworkInterface(Object value) {
		setNetworkInterface((Set<NetworkInterface>) value);
	}
	
	private void setDeviceInterfaces() {
		Object value = networkDevice.getValue(NetworkDevice.INTERFACES);
		setNetworkInterface(value);
	}
	
	public void addListener(TopologyDeviceListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	public void removeListener(TopologyDeviceListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	private void fireInterfacesChange(Set<TopologyInterface> oldInterfaces,
			Set<TopologyInterface> interfaces) {
		Set<TopologyInterface> removedInterfaces = new HashSet<TopologyInterface>(
				oldInterfaces);
		removedInterfaces.removeAll(interfaces);
		
		for (TopologyInterface interfaze : removedInterfaces) {
			fireInterfaceRemoved(interfaze);
		}
		
		Set<TopologyInterface> addedInterfaces = new HashSet<TopologyInterface>(
				interfaces);
		addedInterfaces.removeAll(oldInterfaces);
		
		for (TopologyInterface interfaze : addedInterfaces) {
			fireInterfaceAdded(interfaze);
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