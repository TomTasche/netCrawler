package at.netcrawler.network.model.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.library.util.TypeToken;
import at.netcrawler.network.model.NetworkInterfaceExtension;


public class EthernetInterfaceExtension extends NetworkInterfaceExtension {
	
	private static final long serialVersionUID = 5181624711651457667L;
	
	public static final EthernetInterfaceExtension EXTENSION;
	
	public static final Map<String, TypeToken<?>> EXTENSION_TYPE_MAP;
	
	public static final String ADDRESS = "interface.ethernet.address";
	public static final TypeToken<?> ADDRESS_TYPE = TypeToken
			.get(MACAddress.class);
	
	static {
		Map<String, TypeToken<?>> map = new HashMap<String, TypeToken<?>>();
		map.put(ADDRESS, ADDRESS_TYPE);
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
		
		EXTENSION = new EthernetInterfaceExtension();
	}
	
	public EthernetInterfaceExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}