package at.netcrawler.network.model.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.util.TypeToken;
import at.netcrawler.network.model.NetworkDeviceExtension;


public class SNMPDeviceExtension extends NetworkDeviceExtension {
	
	private static final long serialVersionUID = -5036615619413508399L;
	
	public static final SNMPDeviceExtension EXTENSION;
	
	public static final Map<String, TypeToken<?>> EXTENSION_TYPE_MAP;
	
	public static final String ENGINE_ID = "device.snmp.engineId";
	public static final TypeToken<?> ENGINE_ID_TYPE = TypeToken
			.get(String.class);
	
	static {
		Map<String, TypeToken<?>> map = new HashMap<String, TypeToken<?>>();
		map.put(ENGINE_ID, ENGINE_ID_TYPE);
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
		
		EXTENSION = new SNMPDeviceExtension();
	}
	
	private SNMPDeviceExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}