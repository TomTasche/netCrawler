package at.netcrawler.network.topology;

import java.util.Collections;
import java.util.Set;

import at.andiwand.library.util.CollectionUtil;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;
import at.netcrawler.network.model.extension.CiscoRouterExtension;


public class CiscoRouterIdentificationFactory extends
		DeviceIdentificationFactory {
	
	public static final Set<NetworkDeviceExtension> REQUIRED_EXTENSIONS = Collections
			.unmodifiableSet(CollectionUtil
					.arrayToHashSet(new NetworkDeviceExtension[] {CiscoRouterExtension.EXTENSION}));
	public static final Set<String> REQUIRED_VALUES = Collections
			.unmodifiableSet(CollectionUtil
					.arrayToHashSet(new String[] {CiscoRouterExtension.PROCESSOR_BOARD_ID}));
	
	public CiscoRouterIdentificationFactory() {
		super(REQUIRED_EXTENSIONS, REQUIRED_VALUES);
	}
	
	@Override
	protected CiscoRouterIdentification buildIdenticationImpl(
			NetworkDevice device) {
		String processorBoardId = (String) device
				.getValue(CiscoRouterExtension.PROCESSOR_BOARD_ID);
		return new CiscoRouterIdentification(processorBoardId);
	}
	
}