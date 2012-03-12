package at.netcrawler.network.model.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.andiwand.library.util.TypeToken;
import at.netcrawler.network.CDPNeighbor;
import at.netcrawler.network.model.NetworkDeviceExtension;


public class CiscoDeviceExtension extends NetworkDeviceExtension {
	
	private static final long serialVersionUID = 4682653937036391103L;
	
	public static final CiscoDeviceExtension EXTENSION;
	
	public static final Map<String, TypeToken<?>> EXTENSION_TYPE_MAP;
	
	public static final String CDP_NEIGHBORS = "device.cisco.cdpNeighbors";
	public static final TypeToken<?> CDP_NEIGHBORS_TYPE = new TypeToken<List<CDPNeighbor>>() {};
	
	static {
		Map<String, TypeToken<?>> map = new HashMap<String, TypeToken<?>>();
		map.put(CDP_NEIGHBORS, CDP_NEIGHBORS_TYPE);
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
		
		EXTENSION = new CiscoDeviceExtension();
	}
	
	public CiscoDeviceExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}