package at.netcrawler.network.connection.snmp;

import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.util.ObjectIdentifier;
import at.andiwand.library.util.Timeticks;


public enum SNMPObjectType {
	
	// TODO: complete
	INTEGER(Integer.class),
	UNSIGNED(Long.class),
	STRING(String.class),
	OBJID(ObjectIdentifier.class),
	NULL_OBJ(SNMPNull.class),
	TIMETICKS(Timeticks.class),
	IPADDRESS(IPv4Address.class);
	
	private static final Map<Class<?>, SNMPObjectType> typeMap = new HashMap<Class<?>, SNMPObjectType>();
	
	static {
		for (SNMPObjectType type : values()) {
			typeMap.put(type.objectClass, type);
		}
	}
	
	public static SNMPObjectType getTypeByClass(Class<?> clazz) {
		return typeMap.get(clazz);
	}
	
	private final Class<?> objectClass;
	
	private SNMPObjectType(Class<?> objectClass) {
		this.objectClass = objectClass;
	}
	
	public Class<?> getObjectClass() {
		return objectClass;
	}
	
}