package at.netcrawler.network.model.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.RoutingTable;
import at.netcrawler.network.model.NetworkDeviceExtension;


public class RouterExtension extends NetworkDeviceExtension {
	
	public static final Map<String, Class<?>> EXTENSION_TYPE_MAP;
	
	public static final String ROUTING_TABLE = "device.router.routingTable";
	public static final Class<RoutingTable> ROUTING_TABLE_TYPE = RoutingTable.class;
	
	static {
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		
		map.put(ROUTING_TABLE, ROUTING_TABLE_TYPE);
		
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public RouterExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}