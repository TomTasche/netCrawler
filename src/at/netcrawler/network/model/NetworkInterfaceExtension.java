package at.netcrawler.network.model;

import java.lang.reflect.Type;
import java.util.Map;


public abstract class NetworkInterfaceExtension extends NetworkModelExtension {
	
	private static final long serialVersionUID = -5527726142951707238L;
	
	public static final Class<NetworkInterface> EXTENDED_MODEL_CLASS = NetworkInterface.class;
	
	protected NetworkInterfaceExtension() {
		super(EXTENDED_MODEL_CLASS);
	}
	
	protected NetworkInterfaceExtension(Map<String, Type> extendedTypeMap) {
		super(EXTENDED_MODEL_CLASS, extendedTypeMap);
	}
	
}