package at.netcrawler.network.model.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.util.TypeToken;
import at.netcrawler.network.model.NetworkDeviceExtension;


public class SwitchExtension extends NetworkDeviceExtension {
	
	private static final long serialVersionUID = -1772889164593129184L;
	
	public static final SwitchExtension EXTENSION;
	
	public static final Map<String, TypeToken<?>> EXTENSION_TYPE_MAP;
	
	static {
		Map<String, TypeToken<?>> map = new HashMap<String, TypeToken<?>>();
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
		
		EXTENSION = new SwitchExtension();
	}
	
	public SwitchExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}