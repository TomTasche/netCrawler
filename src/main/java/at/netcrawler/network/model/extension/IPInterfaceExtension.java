package at.netcrawler.network.model.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.ip.SubnetMask;
import at.andiwand.library.util.TypeToken;
import at.netcrawler.network.model.NetworkInterfaceExtension;


public class IPInterfaceExtension extends NetworkInterfaceExtension {
	
	private static final long serialVersionUID = -9085694799945685464L;
	
	public static final IPInterfaceExtension EXTENSION;
	
	public static final Map<String, TypeToken<?>> EXTENSION_TYPE_MAP;
	
	public static final String ADDRESS = "interface.ip.address";
	public static final TypeToken<?> ADDRESS_TYPE = TypeToken
			.get(IPv4Address.class);
	public static final String NETMASK = "interface.ip.netmask";
	public static final TypeToken<?> NETMASK_TYPE = TypeToken
			.get(SubnetMask.class);
	
	static {
		Map<String, TypeToken<?>> map = new HashMap<String, TypeToken<?>>();
		map.put(ADDRESS, ADDRESS_TYPE);
		map.put(NETMASK, NETMASK_TYPE);
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
		
		EXTENSION = new IPInterfaceExtension();
	}
	
	public IPInterfaceExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}