package at.netcrawler.network.model.extension;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.model.NetworkDeviceExtension;


public class CiscoSwitchExtension extends NetworkDeviceExtension {
	
	private static final long serialVersionUID = -2174701981241129411L;
	
	public static final CiscoSwitchExtension EXTENSION;
	
	public static final Map<String, Type> EXTENSION_TYPE_MAP;
	
	public static final String MODEL_NUMBER = "device.cisco.switch.modelNumber";
	public static final Type MODEL_NUMBER_TYPE = String.class;
	
	public static final String SYSTEM_SERIAL_NUMBER = "device.cisco.switch.systemSerialNumber";
	public static final Type SYSTEM_SERIAL_NUMBER_TYPE = String.class;
	
	static {
		Map<String, Type> map = new HashMap<String, Type>();
		map.put(MODEL_NUMBER, MODEL_NUMBER_TYPE);
		map.put(SYSTEM_SERIAL_NUMBER, SYSTEM_SERIAL_NUMBER_TYPE);
		EXTENSION_TYPE_MAP = Collections.unmodifiableMap(map);
		
		EXTENSION = new CiscoSwitchExtension();
	}
	
	private CiscoSwitchExtension() {
		super(EXTENSION_TYPE_MAP);
	}
	
}