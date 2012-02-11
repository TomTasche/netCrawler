package at.netcrawler.network.model;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.CableType;


public class NetworkCable extends NetworkModel {
	
	private static final long serialVersionUID = 5375057361418104267L;
	
	public static final Map<String, Type> TYPE_MAP;
	
	public static final String TYPE = "cable.type";
	public static final Type TYPE_TYPE = CableType.class;
	
	static {
		Map<String, Type> map = new HashMap<String, Type>();
		
		map.put(TYPE, TYPE_TYPE);
		
		TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public NetworkCable() {
		super(TYPE_MAP);
	}
	
}