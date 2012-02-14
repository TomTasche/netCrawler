package at.netcrawler.network.model.extension;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.RoutingTable;
import at.netcrawler.network.model.NetworkDeviceExtension;


public class RouterExtension extends NetworkDeviceExtension {
	
	private static final long serialVersionUID = -1772889164593129184L;
	
	public static final RouterExtension EXTENSION = new RouterExtension();
	
	public static final Map<String, Type> EXTENSION_TYPE_MAP;
	
	public static final String ROUTING_TABLE = "device.router.routingTable";
	public static final Type ROUTING_TABLE_TYPE = RoutingTable.class;
	
	static {
		Map<String, Type> map = new HashMap<String, Type>();
		
		map.put(ROUTING_TABLE, ROUTING_TABLE_TYPE);
		
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public RouterExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}