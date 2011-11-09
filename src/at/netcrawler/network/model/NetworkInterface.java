package at.netcrawler.network.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class NetworkInterface extends NetworkModel {
	
	public static final Map<String, Class<?>> TYPE_MAP;
	
	
	public static final String NAME = "interface.name";
	public static final Class<String> NAME_TYPE = String.class;
	
	public static final String FULL_NAME = "interface.fullName";
	public static final Class<String> FULL_NAME_TYPE = String.class;
	
	
	
	static {
		Map<String, Class<?>> typeMap = new HashMap<String, Class<?>>();
		
		typeMap.put(NAME, NAME_TYPE);
		typeMap.put(FULL_NAME, FULL_NAME_TYPE);
		
		TYPE_MAP = Collections.unmodifiableMap(typeMap);
	}
	
	
	
	
	public NetworkInterface() {
		super(TYPE_MAP);
	}
	
}