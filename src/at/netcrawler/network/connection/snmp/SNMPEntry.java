package at.netcrawler.network.connection.snmp;

import at.andiwand.library.util.ObjectIdentifier;


public class SNMPEntry {
	
	private final ObjectIdentifier oid;
	private final SNMPObjectType type;
	private final Object value;
	
	public SNMPEntry(ObjectIdentifier oid, Object value) {
		this.oid = oid;
		this.type = SNMPObjectType.getTypeByClass(value.getClass());
		if (type == null)
			throw new IllegalArgumentException("Unsupported value!");
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
		
		if (!(obj instanceof SNMPEntry)) return false;
		SNMPEntry entry = (SNMPEntry) obj;
		
		return oid.equals(entry.oid) && value.equals(entry.value);
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		
		result = 31 * result + oid.hashCode();
		result = 31 * result + value.hashCode();
		
		return result;
	}
	
	public ObjectIdentifier getObjectIdentifier() {
		return oid;
	}
	
	public SNMPObjectType getType() {
		return type;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		if (value instanceof SNMPNull) return null;
		return (T) value;
	}
	
}