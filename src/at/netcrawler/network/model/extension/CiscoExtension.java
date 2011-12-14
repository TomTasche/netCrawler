package at.netcrawler.network.model.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.model.NetworkDeviceExtension;


public class CiscoExtension extends NetworkDeviceExtension {
	
	public static final Map<String, Class<?>> EXTENSION_TYPE_MAP;
	
	public static final String MODEL_NUMBER = "device.cisco.modelNumber";
	public static final Class<String> MODEL_NUMBER_TYPE = String.class;
	
	public static final String SYSTEM_SERIAL_NUMBER = "device.cisco.systemSerialNumber";
	public static final Class<String> SYSTEM_SERIAL_NUMBER_TYPE = String.class;
	
	public static final String PROCESSOR_STRING = "device.cisco.processorString";
	public static final Class<String> PROCESSOR_STRING_TYPE = String.class;
	
	static {
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		
		map.put(MODEL_NUMBER, MODEL_NUMBER_TYPE);
		map.put(SYSTEM_SERIAL_NUMBER, SYSTEM_SERIAL_NUMBER_TYPE);
		map.put(PROCESSOR_STRING, PROCESSOR_STRING_TYPE);
		
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public CiscoExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}