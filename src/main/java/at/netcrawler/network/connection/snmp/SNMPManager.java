package at.netcrawler.network.connection.snmp;

import java.io.IOException;
import java.util.List;

import at.andiwand.library.util.ObjectIdentifier;


public interface SNMPManager {
	
	public SNMPVersion getVersion();
	
	public SNMPEntry get(ObjectIdentifier oid) throws IOException;
	
	public List<SNMPEntry> get(ObjectIdentifier... oids) throws IOException;
	
	public SNMPEntry getNext(ObjectIdentifier oid) throws IOException;
	
	public List<SNMPEntry> getNext(ObjectIdentifier... oids) throws IOException;
	
	public List<SNMPEntry> getBulk(ObjectIdentifier oid) throws IOException;
	
	public List<SNMPEntry> getBulk(int maxRepetitions, ObjectIdentifier... oids)
			throws IOException;
	
	public List<SNMPEntry> getBulk(int nonRepeaters, int maxRepetitions,
			ObjectIdentifier... oids) throws IOException;
	
	public SNMPEntry set(ObjectIdentifier oid, Object value) throws IOException;
	
	public SNMPEntry set(SNMPEntry entry) throws IOException;
	
	public List<SNMPEntry> set(SNMPEntry... entries) throws IOException;
	
	public boolean setAndVerify(ObjectIdentifier oid, Object value)
			throws IOException;
	
	public boolean setAndVerify(SNMPEntry... entries) throws IOException;
	
	public List<SNMPEntry> walkNext(ObjectIdentifier oid) throws IOException;
	
	public List<SNMPEntry> walkBulk(ObjectIdentifier oid) throws IOException;
	
	public List<SNMPEntry> walkBulk(int maxRepetitions, ObjectIdentifier oid)
			throws IOException;
	
	public List<SNMPEntry> walk(ObjectIdentifier oid) throws IOException;
	
	public List<SNMPEntry[]> walkNextTable(ObjectIdentifier... oids)
			throws IOException;
	
	public List<SNMPEntry[]> walkBulkTable(ObjectIdentifier... oids)
			throws IOException;
	
	public List<SNMPEntry[]> walkBulkTable(int maxRepetitions,
			ObjectIdentifier... oids) throws IOException;
	
	public List<SNMPEntry[]> walkTable(ObjectIdentifier... oids)
			throws IOException;
	
}