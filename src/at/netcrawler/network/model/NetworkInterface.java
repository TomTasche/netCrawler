package at.netcrawler.network.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.InterfaceType;


public class NetworkInterface extends NetworkModel {
	
	public static final Map<String, Class<?>> TYPE_MAP;
	
	public static final String NAME = "interface.name";
	public static final Class<String> NAME_TYPE = String.class;
	
	public static final String FULL_NAME = "interface.fullName";
	public static final Class<String> FULL_NAME_TYPE = String.class;
	
	public static final String TYPE = "interface.type";
	public static final Class<InterfaceType> TYPE_TYPE = InterfaceType.class;
	
	public static final String ADMIN_STATE = "interface.adminState";
	public static final Class<Boolean> ADMIN_STATE_TYPE = Boolean.class;
	
	public static final String UPTIME = "interface.uptime";
	public static final Class<Long> UPTIME_TYPE = Long.class;
	
	public static final String SPEED = "interface.speed";
	public static final Class<Integer> SPEED_TYPE = Integer.class;
	
	public static final String MTU = "interface.ethernet.mtu";
	public static final Class<Integer> MTU_TYPE = Integer.class;
	
	static {
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		
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