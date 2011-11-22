package at.netcrawler.network.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
	
	public static final String CAPABILITIES = "device.capability";
	public static final Class<Set<Capability>> CAPABILITIES_TYPE = GenericsUtil.castClass(Set.class);
	
	public static final String INTERFACES = "device.interfaces";
	public static final Class<Set<NetworkInterface>> INTERFACES_TYPE = GenericsUtil.castClass(Set.class);
	
	// TODO: add generic information
	
	static {
		Map<String, Class<?>> typeMap = new HashMap<String, Class<?>>();
		
		typeMap.put(IDENTICATION, IDENTICATION_TYPE);
		typeMap.put(HOSTNAME, HOSTNAME_TYPE);
		typeMap.put(SYSTEM, SYSTEM_TYPE);
		typeMap.put(CAPABILITIES, CAPABILITIES_TYPE);
		typeMap.put(INTERFACES, INTERFACES_TYPE);
		// TODO: put generic information
		
		TYPE_MAP = Collections.unmodifiableMap(typeMap);
	}
	
	
	
	
	public NetworkDevice() {
		super(TYPE_MAP);
	}
	
}