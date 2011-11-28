package at.netcrawler.network.connection.snmp;

import java.io.IOException;
import java.util.List;

import at.netcrawler.network.connection.snmp.SNMPObject.Type;


public interface SNMPManager {
	
	public SNMPVersion getVersion();
	
	public SNMPObject get(String oid) throws IOException;
	
	public List<SNMPObject> get(String... oids) throws IOException;
	
	public SNMPObject getNext(String oid) throws IOException;
	
	public List<SNMPObject> getNext(String... oids) throws IOException;
	
	public List<SNMPObject> getBulk(String oid) throws IOException;
	
	public List<SNMPObject> getBulk(int maxRepetitions, String... oids)
			throws IOException;
	
	public List<SNMPObject> getBulk(int nonRepeaters, int maxRepetitions,
			String... oids) throws IOException;
	
	public SNMPObject set(String oid, Type type, String value)
			throws IOException;
	
	public SNMPObject set(SNMPObject object) throws IOException;
	
	public List<SNMPObject> set(SNMPObject... objects) throws IOException;
	
	public boolean setAndVerify(String oid, Type type, String value)
			throws IOException;
	
	public boolean setAndVerify(SNMPObject... objects) throws IOException;
	
	public List<SNMPObject> walkNext(String oid) throws IOException;
	
	public List<SNMPObject> walkBulk(String oid) throws IOException;
	
	public List<SNMPObject> walkBulk(int maxRepetitions, String oid)
			throws IOException;
	
	public List<SNMPObject> walk(String oid) throws IOException;
	
	public List<SNMPObject[]> walkNextTable(String... oids) throws IOException;
	
	public List<SNMPObject[]> walkBulkTable(String... oids) throws IOException;
	
	public List<SNMPObject[]> walkBulkTable(int maxRepetitions, String... oids)
			throws IOException;
	
	public List<SNMPObject[]> walkTable(String... oids) throws IOException;
	
}