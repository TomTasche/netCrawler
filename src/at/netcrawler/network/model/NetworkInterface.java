package at.netcrawler.network.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.util.TypeToken;


public class NetworkInterface extends NetworkModel {
	
	private static final long serialVersionUID = -6324847710385012032L;
	
	public static final Map<String, TypeToken<?>> TYPE_MAP;
	
	public static final String NAME = "interface.name";
	public static final TypeToken<?> NAME_TYPE = TypeToken.get(String.class);
	
	public static final String FULL_NAME = "interface.fullName";
	public static final TypeToken<?> FULL_NAME_TYPE = TypeToken
			.get(String.class);
	
	public static final String TYPE = "interface.type";
	public static final TypeToken<?> TYPE_TYPE = TypeToken
			.get(InterfaceType.class);
	
	public static final String ADMIN_STATE = "interface.adminState";
	public static final TypeToken<?> ADMIN_STATE_TYPE = TypeToken
			.get(Boolean.class);
	
	public static final String UPTIME = "interface.uptime";
	public static final TypeToken<?> UPTIME_TYPE = TypeToken.get(Long.class);
	
	public static final String SPEED = "interface.speed";
	public static final TypeToken<?> SPEED_TYPE = TypeToken.get(Integer.class);
	
	public static final String MTU = "interface.ethernet.mtu";
	public static final TypeToken<?> MTU_TYPE = TypeToken.get(Integer.class);
	
	static {
		Map<String, TypeToken<?>> map = new HashMap<String, TypeToken<?>>();
		map.put(NAME, NAME_TYPE);
		map.put(FULL_NAME, FULL_NAME_TYPE);
		map.put(TYPE, TYPE_TYPE);
		map.put(ADMIN_STATE, ADMIN_STATE_TYPE);
		map.put(UPTIME, UPTIME_TYPE);
		map.put(SPEED, SPEED_TYPE);
		map.put(MTU, MTU_TYPE);
		TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public NetworkInterface() {
		super(TYPE_MAP);
	}
	
}