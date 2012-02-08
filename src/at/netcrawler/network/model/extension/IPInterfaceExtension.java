package at.netcrawler.network.model.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.ip.IPAddress;
import at.andiwand.library.network.ip.SubnetMask;
import at.netcrawler.network.model.NetworkInterfaceExtension;


public class IPInterfaceExtension extends NetworkInterfaceExtension {
	
	public static final Map<String, Class<?>> EXTENSION_TYPE_MAP;
	
	public static final String ADDRESS = "interface.ip.address";
	public static final Class<IPAddress> ADDRESS_TYPE = IPAddress.class;
	public static final String NETMASK = "interface.ip.netmask";
	public static final Class<SubnetMask> NETMASK_TYPE = SubnetMask.class;
	
	static {
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		
		map.put(ADDRESS, ADDRESS_TYPE);
		map.put(NETMASK, NETMASK_TYPE);
		
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public IPInterfaceExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}