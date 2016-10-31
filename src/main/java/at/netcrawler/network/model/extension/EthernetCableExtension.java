package at.netcrawler.network.model.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.util.TypeToken;
import at.netcrawler.network.model.NetworkCableExtension;


public class EthernetCableExtension extends NetworkCableExtension {
	
	private static final long serialVersionUID = -491483133714519504L;
	
	public static final EthernetCableExtension EXTENSION;
	
	public static final Map<String, TypeToken<?>> EXTENSION_TYPE_MAP;
	
	public static final String CROSSOVER = "cable.ethernet.crossover";
	public static final TypeToken<?> CROSSOVER_TYPE = TypeToken
			.get(Boolean.class);
	
	static {
		Map<String, TypeToken<?>> map = new HashMap<String, TypeToken<?>>();
		map.put(CROSSOVER, CROSSOVER_TYPE);
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
		
		EXTENSION = new EthernetCableExtension();
	}
	
	public EthernetCableExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}