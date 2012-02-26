package at.netcrawler.network.model;

import java.util.Map;

import at.andiwand.library.util.TypeToken;


public abstract class NetworkDeviceExtension extends NetworkModelExtension {
	
	private static final long serialVersionUID = 2137962615123976453L;
	
	public static final Class<NetworkDevice> EXTENDED_MODEL_CLASS = NetworkDevice.class;
	
	protected NetworkDeviceExtension() {
		super(EXTENDED_MODEL_CLASS);
	}
	
	protected NetworkDeviceExtension(Map<String, TypeToken<?>> extendedTypeMap) {
		super(EXTENDED_MODEL_CLASS, extendedTypeMap);
	}
	
}