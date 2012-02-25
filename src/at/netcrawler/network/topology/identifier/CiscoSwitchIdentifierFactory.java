package at.netcrawler.network.topology.identifier;

import java.util.Collections;
import java.util.Set;

import at.andiwand.library.util.collections.CollectionUtil;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;
import at.netcrawler.network.model.extension.CiscoSwitchExtension;


public class CiscoSwitchIdentifierFactory extends DeviceIdentifierFactory {
	
	public static final Set<NetworkDeviceExtension> REQUIRED_EXTENSIONS = Collections
			.unmodifiableSet(CollectionUtil
					.arrayToHashSet(new NetworkDeviceExtension[] {CiscoSwitchExtension.EXTENSION}));
	public static final Set<String> REQUIRED_VALUES = Collections
			.unmodifiableSet(CollectionUtil
					.arrayToHashSet(new String[] {CiscoSwitchExtension.SYSTEM_SERIAL_NUMBER}));
	
	public CiscoSwitchIdentifierFactory() {
		super(REQUIRED_EXTENSIONS, REQUIRED_VALUES);
	}
	
	@Override
	protected CiscoSwitchIdentifier buildIdentifierImpl(NetworkDevice device) {
		String systemSerialNumber = (String) device
				.getValue(CiscoSwitchExtension.SYSTEM_SERIAL_NUMBER);
		return new CiscoSwitchIdentifier(systemSerialNumber);
	}
	
}