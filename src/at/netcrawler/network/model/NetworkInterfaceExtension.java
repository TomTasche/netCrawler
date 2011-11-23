package at.netcrawler.network.model;

import java.util.Map;
import java.util.Set;


public abstract class NetworkInterfaceExtension extends
		AbstractNetworkModelExtension {
	
	public static final Class<NetworkInterface> EXTENDED_MODEL_CLASS = NetworkInterface.class;
	
	protected NetworkInterfaceExtension() {
		super(EXTENDED_MODEL_CLASS);
	}
	protected NetworkInterfaceExtension(Map<String, Class<?>> extendedTypeMap) {
		super(EXTENDED_MODEL_CLASS, extendedTypeMap);
	}
	protected NetworkInterfaceExtension(
			Set<NetworkInterfaceExtension> requiredExtensions,
			Map<String, Class<?>> extendedTypeMap) {
		super(EXTENDED_MODEL_CLASS, requiredExtensions, extendedTypeMap);
	}
	
}