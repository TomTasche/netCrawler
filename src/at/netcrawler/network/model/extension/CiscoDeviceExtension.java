package at.netcrawler.network.model.extension;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.CDPNeighbors;
import at.netcrawler.network.model.NetworkDeviceExtension;


public class CiscoDeviceExtension extends NetworkDeviceExtension {
	
	private static final long serialVersionUID = 4682653937036391103L;
	
	public static final Map<String, Type> EXTENSION_TYPE_MAP;
	
	public static final String MODEL_NUMBER = "device.cisco.modelNumber";
	public static final Type MODEL_NUMBER_TYPE = String.class;
	
	public static final String SYSTEM_SERIAL_NUMBER = "device.cisco.systemSerialNumber";
	public static final Type SYSTEM_SERIAL_NUMBER_TYPE = String.class;
	
	public static final String PROCESSOR_STRING = "device.cisco.processorString";
	public static final Type PROCESSOR_STRING_TYPE = String.class;
	
	public static final String CDP_NEIGHBORS = "device.cisco.cdpNeighbors";
	public static final Type CDP_NEIGHBORS_TYPE = CDPNeighbors.class;
	
	static {
		Map<String, Type> map = new HashMap<String, Type>();
		
		map.put(MODEL_NUMBER, MODEL_NUMBER_TYPE);
		map.put(SYSTEM_SERIAL_NUMBER, SYSTEM_SERIAL_NUMBER_TYPE);
		map.put(PROCESSOR_STRING, PROCESSOR_STRING_TYPE);
		map.put(CDP_NEIGHBORS, CDP_NEIGHBORS_TYPE);
		
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public CiscoDeviceExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}