package at.netcrawler.network.topology;

import java.util.Collections;
import java.util.Set;

import at.andiwand.library.util.CollectionUtil;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;
import at.netcrawler.network.model.extension.SNMPDeviceExtension;


public class SNMPDeviceIdentificationFactory extends
		DeviceIdentificationFactory {
	
	public static final Set<NetworkDeviceExtension> REQUIRED_EXTENSIONS = Collections
			.unmodifiableSet(CollectionUtil
					.arrayToHashSet(new NetworkDeviceExtension[] {SNMPDeviceExtension.EXTENSION}));
	public static final Set<String> REQUIRED_VALUES = Collections
			.unmodifiableSet(CollectionUtil
					.arrayToHashSet(new String[] {SNMPDeviceExtension.ENGINE_ID}));
	
	public SNMPDeviceIdentificationFactory() {
		super(REQUIRED_EXTENSIONS, REQUIRED_VALUES);
	}
	
	@Override
	protected SNMPDeviceIdentification buildIdenticationImpl(
			NetworkDevice device) {
		String engineId = (String) device
				.getValue(SNMPDeviceExtension.ENGINE_ID);
		return new SNMPDeviceIdentification(engineId);
	}
	
}