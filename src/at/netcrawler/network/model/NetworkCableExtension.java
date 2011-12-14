package at.netcrawler.network.model;

import java.util.Map;
import java.util.Set;


public abstract class NetworkCableExtension extends
		AbstractNetworkModelExtension<NetworkCableExtension> {
	
	public static final Class<NetworkCable> EXTENDED_MODEL_CLASS = NetworkCable.class;
	
	protected NetworkCableExtension() {
		super(EXTENDED_MODEL_CLASS);
	}
	
	protected NetworkCableExtension(Map<String, Class<?>> extendedTypeMap) {
		super(EXTENDED_MODEL_CLASS, extendedTypeMap);
	}
	
	protected NetworkCableExtension(
			Set<NetworkCableExtension> requiredExtensions,
			Map<String, Class<?>> extendedTypeMap) {
		super(EXTENDED_MODEL_CLASS, requiredExtensions, extendedTypeMap);
	}
	
}