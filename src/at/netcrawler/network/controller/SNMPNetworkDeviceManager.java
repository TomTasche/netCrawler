package at.netcrawler.network.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.snmp4j.PDU;
import org.snmp4j.smi.VariableBinding;

import at.andiwand.library.network.mac.MACAddress;
import at.netcrawler.network.Capability;
import at.netcrawler.network.SNMPConnection;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.model.NetworkInterfaceExtension;
import at.netcrawler.network.model.NetworkInterfaceExtensionSet;


public class SNMPNetworkDeviceManager extends NetworkDeviceManager {
	
	private SNMPConnection connection;
	
	
	
	public SNMPNetworkDeviceManager(InetAddress address, int port)
			throws IOException {
		super();
		
		init(address, port);
	}
	public SNMPNetworkDeviceManager(NetworkDevice device, InetAddress address,
			int port) throws IOException {
		super(device);
		
		init(address, port);
	}
	
	
	
	private void init(InetAddress address, int port) throws IOException {
		connection = new SNMPConnection(address, port);
	}
	
	
	@Override
	public String getIdentication() throws IOException {
		PDU response = connection.fetch("1.3.6.1.2.1.1.2.0");
		VariableBinding variableBinding = response.get(0);
		
		return variableBinding.getVariable().toString();
	}
	
	@Override
	public String getHostname() throws IOException {
		PDU response = connection.fetch("1.3.6.1.2.1.1.5.0");
		VariableBinding variableBinding = response.get(0);
		
		return variableBinding.getVariable().toString();
	}
	
	@Override
	public String getSystem() throws IOException {
		PDU response = connection.fetch("1.3.6.1.2.1.1.1.0");
		VariableBinding variableBinding = response.get(0);
		
		return variableBinding.getVariable().toString();
	}
	
	@Override
	public Set<Capability> getCapabilities() throws IOException {
		PDU response = connection.fetch("1.3.6.1.2.1.1.7.0");
		VariableBinding variableBinding = response.get(0);
		int servicesInt = variableBinding.getVariable().toInt();
		
		Set<Capability> result = new HashSet<Capability>();
		if ((servicesInt & 0x04) != 0)
			result.add(Capability.ROUTER);
		return result;
	}
	
	@Override
	public Set<NetworkInterface> getInterfaces() throws IOException {
		List<PDU> interfaceNumbers = connection.fetchRange("1.3.6.1.2.1.2.2.1.1");
		List<PDU> interfaceDescriptions = connection.fetchRange("1.3.6.1.2.1.2.2.1.2");
		List<PDU> interfaceAddresses = connection.fetchRange("1.3.6.1.2.1.2.2.1.6");
		List<PDU> interfaceNames = connection.fetchRange("1.3.6.1.2.1.31.1.1.1.1");
		Set<NetworkInterface> interfaces = new HashSet<NetworkInterface>();
		VariableBinding variableBinding;
		
		for (int i = 0; i < interfaceNumbers.size(); i++) {
			NetworkInterface newInterface = new NetworkInterface();
			
			variableBinding = interfaceDescriptions.get(i).get(0);
			newInterface.setValue(NetworkInterface.FULL_NAME, variableBinding.getVariable().toString());
			
			variableBinding = interfaceAddresses.get(i).get(0);
			String macAddressString = variableBinding.getVariable().toString();
			if (!macAddressString.isEmpty()) {
				newInterface.addExtensionSet(NetworkInterfaceExtensionSet.ETHERNET);
				newInterface.setValue(NetworkInterfaceExtension.ETHERNET_ADDRESS, MACAddress.getByAddress(macAddressString));
			}
			
			variableBinding = interfaceNames.get(i).get(0);
			newInterface.setValue(NetworkInterface.NAME, variableBinding.getVariable().toString());
			
			interfaces.add(newInterface);
		}
		
		return interfaces;
	}
	
	
	@Override
	public void setHostname(String hostname) throws IOException {
		PDU response = connection.set("1.3.6.1.2.1.1.5.0", hostname);
		System.out.println(response);
	}
	
	
	
	public static void main(String[] args) throws Throwable {
		InetAddress address = InetAddress.getByName("192.168.0.2");
		int port = 161;
		
		SNMPNetworkDeviceManager deviceManager = new SNMPNetworkDeviceManager(address, port);
		
		deviceManager.fetchDevice();
		System.out.println(deviceManager.getDevice());
		
		deviceManager.setHostname("HAHA");
		
		deviceManager.fetchDevice();
		System.out.println(deviceManager.getDevice());
	}
	
}