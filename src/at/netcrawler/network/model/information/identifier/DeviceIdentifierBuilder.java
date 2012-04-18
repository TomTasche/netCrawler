package at.netcrawler.network.model.information.identifier;

import java.util.LinkedList;
import java.util.List;

import at.netcrawler.network.model.NetworkDevice;


// TODO: super class
// TODO: device manager level
public final class DeviceIdentifierBuilder {
	
	public static DeviceIdentifierBuilder getDefaultBuilder() {
		DeviceIdentifierBuilder result = new DeviceIdentifierBuilder();
		
		result.addFactory(new CiscoSwitchIdentifierFactory());
		result.addFactory(new CiscoRouterIdentifierFactory());
		result.addFactory(new SNMPDeviceIdentifierFactory());
		
		return result;
	}
	
	private final List<DeviceIdentifierFactory> factories = new LinkedList<DeviceIdentifierFactory>();
	
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