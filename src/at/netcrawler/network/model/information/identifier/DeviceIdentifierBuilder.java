package at.netcrawler.network.model.information.identifier;

import java.util.LinkedList;
import java.util.List;

import at.netcrawler.network.model.NetworkDevice;


// TODO: super class
// TODO: device manager level
public final class DeviceIdentifierBuilder {
	
	private final List<DeviceIdentifierFactory> factories = new LinkedList<DeviceIdentifierFactory>();
	
	public DeviceIdentifierBuilder() {
		addFactory(new CiscoSwitchIdentifierFactory());
		addFactory(new CiscoRouterIdentifierFactory());
		addFactory(new SNMPDeviceIdentifierFactory());
	}
	
	public void addFactory(DeviceIdentifierFactory factory) {
		factories.add(factory);
	}
	
	public void removeFactory(DeviceIdentifierFactory factory) {
		factories.remove(factory);
	}
	
	public DeviceIdentifier getIdentification(NetworkDevice device) {
		for (DeviceIdentifierFactory factory : factories) {
			DeviceIdentifier identification = factory.build(device);
			if (identification != null) return identification;
		}
		
		return null;
	}
	
}