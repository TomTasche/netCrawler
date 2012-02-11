package at.netcrawler.network.model;

import java.lang.reflect.Type;
import java.util.Map;


public abstract class NetworkDeviceExtension extends NetworkModelExtension {
	
	private static final long serialVersionUID = 2137962615123976453L;
	
	public static final Class<NetworkDevice> EXTENDED_MODEL_CLASS = NetworkDevice.class;
	
	protected NetworkDeviceExtension() {
		super(EXTENDED_MODEL_CLASS);
	}
	
	protected NetworkDeviceExtension(Map<String, Type> extendedTypeMap) {
		super(EXTENDED_MODEL_CLASS, extendedTypeMap);
	}
	
}