package at.netcrawler.network.model.extension;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.ip.SubnetMask;
import at.netcrawler.network.model.NetworkInterfaceExtension;


public class IPInterfaceExtension extends NetworkInterfaceExtension {
	
	private static final long serialVersionUID = -9085694799945685464L;
	
	public static final Map<String, Type> EXTENSION_TYPE_MAP;
	
	public static final String ADDRESS = "interface.ip.address";
	public static final Type ADDRESS_TYPE = IPv4Address.class;
	public static final String NETMASK = "interface.ip.netmask";
	public static final Type NETMASK_TYPE = SubnetMask.class;
	
	static {
		Map<String, Type> map = new HashMap<String, Type>();
		
		map.put(ADDRESS, ADDRESS_TYPE);
		map.put(NETMASK, NETMASK_TYPE);
		
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public IPInterfaceExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}