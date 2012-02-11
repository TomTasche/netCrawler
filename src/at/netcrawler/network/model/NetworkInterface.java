package at.netcrawler.network.model;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.InterfaceType;


public class NetworkInterface extends NetworkModel {
	
	private static final long serialVersionUID = -6324847710385012032L;
	
	public static final Map<String, Type> TYPE_MAP;
	
	public static final String NAME = "interface.name";
	public static final Type NAME_TYPE = String.class;
	
	public static final String FULL_NAME = "interface.fullName";
	public static final Type FULL_NAME_TYPE = String.class;
	
	public static final String TYPE = "interface.type";
	public static final Type TYPE_TYPE = InterfaceType.class;
	
	public static final String ADMIN_STATE = "interface.adminState";
	public static final Type ADMIN_STATE_TYPE = Boolean.class;
	
	public static final String UPTIME = "interface.uptime";
	public static final Type UPTIME_TYPE = Long.class;
	
	public static final String SPEED = "interface.speed";
	public static final Type SPEED_TYPE = Integer.class;
	
	public static final String MTU = "interface.ethernet.mtu";
	public static final Type MTU_TYPE = Integer.class;
	
	static {
		Map<String, Type> map = new HashMap<String, Type>();
		
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