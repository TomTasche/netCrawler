package at.netcrawler.network.model;

import java.util.Map;
import java.util.Set;


public abstract class NetworkDeviceExtension extends
		AbstractNetworkModelExtension<NetworkDeviceExtension> {
	
	public static final Class<NetworkDevice> EXTENDED_MODEL_CLASS = NetworkDevice.class;
	
	protected NetworkDeviceExtension() {
		super(EXTENDED_MODEL_CLASS);
	}
	
	protected NetworkDeviceExtension(Map<String, Class<?>> extendedTypeMap) {
		super(EXTENDED_MODEL_CLASS, extendedTypeMap);
	}
	
	protected NetworkDeviceExtension(
			Set<NetworkDeviceExtension> requiredExtensions,
			Map<String, Class<?>> extendedTypeMap) {
		super(EXTENDED_MODEL_CLASS, requiredExtensions, extendedTypeMap);
	}
	
}