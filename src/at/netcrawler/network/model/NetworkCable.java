package at.netcrawler.network.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.util.TypeToken;


public class NetworkCable extends NetworkModel {
	
	private static final long serialVersionUID = 5375057361418104267L;
	
	public static final Map<String, TypeToken<?>> TYPE_MAP;
	
	public static final String TYPE = "cable.type";
	public static final TypeToken<?> TYPE_TYPE = TypeToken.get(CableType.class);
	
	static {
		Map<String, TypeToken<?>> map = new HashMap<String, TypeToken<?>>();
		map.put(TYPE, TYPE_TYPE);
		TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public NetworkCable() {
		super(TYPE_MAP);
	}
	
}