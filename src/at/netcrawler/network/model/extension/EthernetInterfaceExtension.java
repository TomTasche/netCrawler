package at.netcrawler.network.model.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.mac.MACAddress;
import at.netcrawler.network.model.NetworkInterfaceExtension;


public class EthernetInterfaceExtension extends NetworkInterfaceExtension {
	
	public static final Map<String, Class<?>> EXTENSION_TYPE_MAP;
	
	public static final String ADDRESS = "interface.ethernet.address";
	public static final Class<MACAddress> ADDRESS_TYPE = MACAddress.class;
	
	static {
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		
		map.put(ADDRESS, ADDRESS_TYPE);
		
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public EthernetInterfaceExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}