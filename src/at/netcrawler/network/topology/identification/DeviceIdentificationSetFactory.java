package at.netcrawler.network.topology.identification;

import java.util.HashSet;
import java.util.Set;

import at.netcrawler.network.model.NetworkDevice;


public final class DeviceIdentificationSetFactory {
	
	private final Set<DeviceIdentificationFactory> identificationFactories = new HashSet<DeviceIdentificationFactory>();
	
	public DeviceIdentificationSetFactory() {
		addIdenticationFactory(new CiscoSwitchIdentificationFactory());
		addIdenticationFactory(new CiscoRouterIdentificationFactory());
		addIdenticationFactory(new SNMPDeviceIdentificationFactory());
	}
	
	public void addIdenticationFactory(
			DeviceIdentificationFactory identificationFactory) {
		identificationFactories.add(identificationFactory);
	}
	
	public void removeIdenticationFactory(
			DeviceIdentificationFactory identificationFactory) {
		identificationFactories.remove(identificationFactory);
	}
	
	public DeviceIdentificationSet buildIdentificationSet(NetworkDevice device) {
		DeviceIdentificationSet result = new DeviceIdentificationSet();
		
		for (DeviceIdentificationFactory identificationFactory : identificationFactories) {
			DeviceIdentification identification = identificationFactory
					.buildIdentication(device);
			if (identification == null) continue;
			result.addIdentification(identification);
		}
		
		return result;
	}
	
}