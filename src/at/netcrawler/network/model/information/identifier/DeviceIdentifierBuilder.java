package at.netcrawler.network.model.information.identifier;

import java.util.LinkedList;
import java.util.List;

import at.netcrawler.network.model.NetworkDevice;


public final class DeviceIdentifierBuilder {
	
	private final List<DeviceIdentifierFactory> identifierFactories = new LinkedList<DeviceIdentifierFactory>();
	
	public DeviceIdentifierBuilder() {
		addIdentifierFactory(new CiscoSwitchIdentifierFactory());
		addIdentifierFactory(new CiscoRouterIdentifierFactory());
		addIdentifierFactory(new SNMPDeviceIdentifierFactory());
	}
	
	public void addIdentifierFactory(
			DeviceIdentifierFactory identificationFactory) {
		identifierFactories.add(identificationFactory);
	}
	
	public void removeIdentifierFactory(
			DeviceIdentifierFactory identificationFactory) {
		identifierFactories.remove(identificationFactory);
	}
	
	public DeviceIdentifier getIdentification(NetworkDevice device) {
		for (DeviceIdentifierFactory identificationFactory : identifierFactories) {
			DeviceIdentifier identification = identificationFactory
					.build(device);
			if (identification != null) return identification;
		}
		
		return null;
	}
	
}