package at.netcrawler.network.model.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.util.TypeToken;
import at.netcrawler.network.model.NetworkDeviceExtension;


public class CiscoRouterExtension extends NetworkDeviceExtension {
	
	private static final long serialVersionUID = -5164771254516577449L;
	
	public static final CiscoRouterExtension EXTENSION;
	
	public static final Map<String, TypeToken<?>> EXTENSION_TYPE_MAP;
	
	public static final String PROCESSOR_BOARD_ID = "device.cisco.router.modelNumber";
	public static final TypeToken<?> PROCESSOR_BOARD_ID_TYPE = TypeToken
			.get(String.class);
	
	static {
		Map<String, TypeToken<?>> map = new HashMap<String, TypeToken<?>>();
		map.put(PROCESSOR_BOARD_ID, PROCESSOR_BOARD_ID_TYPE);
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
		
		EXTENSION = new CiscoRouterExtension();
	}
	
	public CiscoRouterExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}