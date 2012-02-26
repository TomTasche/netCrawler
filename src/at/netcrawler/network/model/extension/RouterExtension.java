package at.netcrawler.network.model.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.andiwand.library.util.TypeToken;
import at.netcrawler.network.Route;
import at.netcrawler.network.model.NetworkDeviceExtension;


public class RouterExtension extends NetworkDeviceExtension {
	
	private static final long serialVersionUID = -1772889164593129184L;
	
	public static final RouterExtension EXTENSION;
	
	public static final Map<String, TypeToken<?>> EXTENSION_TYPE_MAP;
	
	public static final String ROUTING_TABLE = "device.router.routingTable";
	public static final TypeToken<?> ROUTING_TABLE_TYPE = new TypeToken<List<Route>>() {};
	
	static {
		Map<String, TypeToken<?>> map = new HashMap<String, TypeToken<?>>();
		map.put(ROUTING_TABLE, ROUTING_TABLE_TYPE);
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
		
		EXTENSION = new RouterExtension();
	}
	
	private RouterExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}