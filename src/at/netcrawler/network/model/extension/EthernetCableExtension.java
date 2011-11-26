package at.netcrawler.network.model.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.model.NetworkCableExtension;


public class EthernetCableExtension extends NetworkCableExtension {
	
	public static final Map<String, Class<?>> EXTENSION_TYPE_MAP;
	
	public static final String CROSSOVER = "cable.ethernet.crossover";
	public static final Class<Boolean> CROSSOVER_TYPE = Boolean.class;
	
	static {
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		
		map.put(CROSSOVER, CROSSOVER_TYPE);
		
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public EthernetCableExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}