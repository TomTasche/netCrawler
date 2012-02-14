package at.netcrawler.network.model.extension;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.model.NetworkDeviceExtension;


public class SNMPDeviceExtension extends NetworkDeviceExtension {
	
	private static final long serialVersionUID = -5036615619413508399L;
	
	public static final SNMPDeviceExtension EXTENSION = new SNMPDeviceExtension();
	
	public static final Map<String, Type> EXTENSION_TYPE_MAP;
	
	public static final String ENGINE_ID = "device.snmp.engineId";
	public static final Type ENGINE_ID_TYPE = String.class;
	
	static {
		Map<String, Type> map = new HashMap<String, Type>();
		
		map.put(ENGINE_ID, ENGINE_ID_TYPE);
		
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public SNMPDeviceExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}