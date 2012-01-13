package at.netcrawler.network.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.network.ip.IPAddress;
import at.andiwand.library.util.GenericsUtil;
import at.netcrawler.network.Capability;


public class NetworkDevice extends NetworkModel {
	
	public static final Map<String, Class<?>> TYPE_MAP;
	
	public static final String IDENTICATION = "device.identication";
	public static final Class<String> IDENTICATION_TYPE = String.class;
	
	public static final String HOSTNAME = "device.hostname";
	public static final Class<String> HOSTNAME_TYPE = String.class;
	
	public static final String SYSTEM = "device.system";
	public static final Class<String> SYSTEM_TYPE = String.class;
	
	public static final String UPTIME = "device.uptime";
	public static final Class<Long> UPTIME_TYPE = Long.class;
	
	public static final String CAPABILITIES = "device.capability";
	public static final Class<Set<Capability>> CAPABILITIES_TYPE = GenericsUtil.castClass(Set.class);
	
	public static final String INTERFACES = "device.interfaces";
	public static final Class<Set<NetworkInterface>> INTERFACES_TYPE = GenericsUtil.castClass(Set.class);
	
	public static final String MANAGEMENT_ADDRESSES = "device.managementIpSet";
	public static final Class<Set<IPAddress>> MANAGEMENT_ADDRESSES_TYPE = GenericsUtil.castClass(Set.class);
	
	// TODO: add generic information
	
	static {
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		
		map.put(IDENTICATION, IDENTICATION_TYPE);
		map.put(HOSTNAME, HOSTNAME_TYPE);
		map.put(SYSTEM, SYSTEM_TYPE);
		map.put(UPTIME, UPTIME_TYPE);
		map.put(CAPABILITIES, CAPABILITIES_TYPE);
		map.put(INTERFACES, INTERFACES_TYPE);
		map.put(MANAGEMENT_ADDRESSES, MANAGEMENT_ADDRESSES_TYPE);
		// TODO: put generic information
		
		TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public NetworkDevice() {
		super(TYPE_MAP);
	}
	
}