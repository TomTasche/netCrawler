package at.netcrawler.network.connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.network.ip.IPAddress;
import at.netcrawler.network.accessor.DeviceAccessor;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.snmp.LocalSNMPGateway;
import at.netcrawler.network.connection.ssh.LocalSSHGateway;
import at.netcrawler.network.connection.telnet.LocalTelnetGateway;
import at.netcrawler.network.model.NetworkDevice;


public class ConnectionBuilder {
	
	// TODO: complete
	public static ConnectionBuilder getLocalConnectionBuilder() {
		ConnectionBuilder result = new ConnectionBuilder();
		
		result.addConnectionGateway(new LocalTelnetGateway());
		result.addConnectionGateway(new LocalSSHGateway());
		result.addConnectionGateway(new LocalSNMPGateway());
		
		return result;
	}
	
	private Map<ConnectionType, ConnectionGateway> gatewayMap = new HashMap<ConnectionType, ConnectionGateway>();
	
	public ConnectionBuilder() {}
	
	public ConnectionBuilder(ConnectionGateway... connectionGateways) {
		for (ConnectionGateway connectionGateway : connectionGateways) {
			addConnectionGateway(connectionGateway);
		}
	}
	
	public void addConnectionGateway(ConnectionGateway connectionGateway) {
		gatewayMap
				.put(connectionGateway.getConnectionType(), connectionGateway);
	}
	
	public void removeConnectionGateway(ConnectionType connectionType) {
		gatewayMap.remove(connectionType);
	}
	
	public void removeConnectionGateway(ConnectionGateway connectionGateway) {
		removeConnectionGateway(connectionGateway.getConnectionType());
	}
	
	@SuppressWarnings("unchecked")
	public <C extends Connection> C openConnection(
			ConnectionType connectionType, DeviceAccessor deviceAccessor,
			ConnectionSettings connectionSettings) throws IOException {
		ConnectionGateway gateway = gatewayMap.get(connectionType);
		if (gateway == null) return null;
		
		return (C) gateway.openConnection(deviceAccessor, connectionSettings);
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public <C extends Connection> C openConnection(
			ConnectionType connectionType, NetworkDevice networkDevice,
			ConnectionSettings connectionSettings) throws IOException {
		ConnectionGateway gateway = gatewayMap.get(connectionType);
		if (gateway == null) return null;
		
		Set<IPAddress> managementAddresses = networkDevice
				.getValue(NetworkDevice.MANAGEMENT_ADDRESSES);
		if (managementAddresses.isEmpty())
			throw new IndexOutOfBoundsException();
		IPDeviceAccessor deviceAccessor = new IPDeviceAccessor(
				managementAddresses.iterator().next());
		return (C) gateway.openConnection(deviceAccessor, connectionSettings);
	}
}