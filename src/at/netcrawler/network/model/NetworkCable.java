package at.netcrawler.network.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class NetworkCable extends NetworkModel {
	
	public static final Map<String, Class<?>> TYPE_MAP;
	
	
	public static final String TYPE = "cable.type";
	public static final Class<String> TYPE_TYPE = String.class;
	
	
	
	static {
		Map<String, Class<?>> typeMap = new HashMap<String, Class<?>>();
		
		typeMap.put(TYPE, TYPE_TYPE);
		
		TYPE_MAP = Collections.unmodifiableMap(typeMap);
	}
	
	
	
	
	public NetworkCable() {
		super(TYPE_MAP);
	}
	
}