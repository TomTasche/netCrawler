package at.netcrawler.network.model.extension;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.model.NetworkCableExtension;


public class EthernetCableExtension extends NetworkCableExtension {
	
	private static final long serialVersionUID = -491483133714519504L;
	
	public static final Map<String, Type> EXTENSION_TYPE_MAP;
	
	public static final String CROSSOVER = "cable.ethernet.crossover";
	public static final Type CROSSOVER_TYPE = Boolean.class;
	
	static {
		Map<String, Type> map = new HashMap<String, Type>();
		
		map.put(CROSSOVER, CROSSOVER_TYPE);
		
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public EthernetCableExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}