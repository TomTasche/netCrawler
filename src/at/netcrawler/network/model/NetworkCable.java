package at.netcrawler.network.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.CableType;


public class NetworkCable extends NetworkModel {
	
	public static final Map<String, Class<?>> TYPE_MAP;
	
	public static final String TYPE = "cable.type";
	public static final Class<CableType> TYPE_TYPE = CableType.class;
	
	static {
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		
		map.put(TYPE, TYPE_TYPE);
		
		TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public NetworkCable() {
		super(TYPE_MAP);
	}
	
}