package at.netcrawler.network.model.extension;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.model.NetworkDeviceExtension;


public class CiscoRouterExtension extends NetworkDeviceExtension {
	
	private static final long serialVersionUID = -5164771254516577449L;
	
	public static final CiscoRouterExtension EXTENSION = new CiscoRouterExtension();
	
	public static final Map<String, Type> EXTENSION_TYPE_MAP;
	
	public static final String PROCESSOR_BOARD_ID = "device.cisco.router.modelNumber";
	public static final Type PROCESSOR_BOARD_ID_TYPE = String.class;
	
	static {
		Map<String, Type> map = new HashMap<String, Type>();
		
		map.put(PROCESSOR_BOARD_ID, PROCESSOR_BOARD_ID_TYPE);
		
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public CiscoRouterExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}