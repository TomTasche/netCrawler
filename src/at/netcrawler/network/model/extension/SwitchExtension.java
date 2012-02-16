package at.netcrawler.network.model.extension;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.model.NetworkDeviceExtension;


public class SwitchExtension extends NetworkDeviceExtension {
	
	private static final long serialVersionUID = -1772889164593129184L;
	
	public static final SwitchExtension EXTENSION;
	
	public static final Map<String, Type> EXTENSION_TYPE_MAP;
	
	static {
		Map<String, Type> map = new HashMap<String, Type>();
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
		
		EXTENSION = new SwitchExtension();
	}
	
	private SwitchExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}