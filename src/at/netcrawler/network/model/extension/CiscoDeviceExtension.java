package at.netcrawler.network.model.extension;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.CDPNeighbors;
import at.netcrawler.network.model.NetworkDeviceExtension;


public class CiscoDeviceExtension extends NetworkDeviceExtension {
	
	private static final long serialVersionUID = 4682653937036391103L;
	
	public static final CiscoDeviceExtension EXTENSION = new CiscoDeviceExtension();
	
	public static final Map<String, Type> EXTENSION_TYPE_MAP;
	
	public static final String CDP_NEIGHBORS = "device.cisco.cdpNeighbors";
	public static final Type CDP_NEIGHBORS_TYPE = CDPNeighbors.class;
	
	static {
		Map<String, Type> map = new HashMap<String, Type>();
		
		map.put(CDP_NEIGHBORS, CDP_NEIGHBORS_TYPE);
		
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public CiscoDeviceExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}