package at.netcrawler.network.model;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.util.GenericsUtil;
import at.netcrawler.network.Capability;


public class NetworkDevice extends NetworkModel {
	
	private static final long serialVersionUID = -1850566228109843184L;
	
	public static final Map<String, Type> TYPE_MAP;
	
	public static final String HOSTNAME = "device.hostname";
	public static final Type HOSTNAME_TYPE = String.class;
	
	public static final String SYSTEM = "device.system";
	public static final Type SYSTEM_TYPE = String.class;
	
	public static final String UPTIME = "device.uptime";
	public static final Type UPTIME_TYPE = Long.class;
	
	public static final String CAPABILITIES = "device.capability";
	public static final Type CAPABILITIES_TYPE = new GenericsUtil.TypeToken<Set<Capability>>() {}.getType();
	
	public static final String MAJOR_CAPABILITY = "device.majorCapability";
	public static final Type MAJOR_CAPABILITY_TYPE = Capability.class;
	
	public static final String INTERFACES = "device.interfaces";
	public static final Type INTERFACES_TYPE = new GenericsUtil.TypeToken<Set<NetworkInterface>>() {}.getType();
	
	public static final String MANAGEMENT_ADDRESSES = "device.managementIpSet";
	public static final Type MANAGEMENT_ADDRESSES_TYPE = new GenericsUtil.TypeToken<Set<IPv4Address>>() {}.getType();
	
	static {
		Map<String, Type> map = new HashMap<String, Type>();
		
		map.put(HOSTNAME, HOSTNAME_TYPE);
		map.put(SYSTEM, SYSTEM_TYPE);
		map.put(UPTIME, UPTIME_TYPE);
		map.put(CAPABILITIES, CAPABILITIES_TYPE);
		map.put(MAJOR_CAPABILITY, MAJOR_CAPABILITY_TYPE);
		map.put(INTERFACES, INTERFACES_TYPE);
		map.put(MANAGEMENT_ADDRESSES, MANAGEMENT_ADDRESSES_TYPE);
		
		TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public NetworkDevice() {
		super(TYPE_MAP);
	}
	
}