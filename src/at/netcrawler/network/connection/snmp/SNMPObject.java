package at.netcrawler.network.connection.snmp;

public class SNMPObject {
	
	public static enum Type {
		INTEGER, UNSIGNED, STRING, HEX_STRING, DECIMAL_STRING, NULLOBJ, OBJID,
		TIMETICKS, IPADDRESS, BITS;
	}
	
	private final String oid;
	private final Type type;
	private final String value;
	
	public SNMPObject(String oid, Type type, String value) {
		this.oid = oid;
		this.type = type;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return oid + " = " + type + ": " + value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof SNMPObject)) return false;
		SNMPObject object = (SNMPObject) obj;
		
		return oid.equals(object.oid) && type.equals(object.type)
				&& value.equals(object.value);
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		
		result = 31 * result + oid.hashCode();
		result = 31 * result + type.hashCode();
		result = 31 * result + value.hashCode();
		
		return result;
	}
	
	public String getOid() {
		return oid;
	}
	
	public Type getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	
}