package at.netcrawler.network.model.extension;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.mac.MACAddress;
import at.netcrawler.network.model.NetworkInterfaceExtension;


public class EthernetInterfaceExtension extends NetworkInterfaceExtension {
	
	private static final long serialVersionUID = 5181624711651457667L;
	
	public static final EthernetInterfaceExtension EXTENSION = new EthernetInterfaceExtension();
	
	public static final Map<String, Type> EXTENSION_TYPE_MAP;
	
	public static final String ADDRESS = "interface.ethernet.address";
	public static final Type ADDRESS_TYPE = MACAddress.class;
	
	static {
		Map<String, Type> map = new HashMap<String, Type>();
		
		map.put(ADDRESS, ADDRESS_TYPE);
		
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public EthernetInterfaceExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}