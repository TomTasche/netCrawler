package at.netcrawler.network.connection.snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.netcrawler.network.IPDeviceAccessor;
import at.netcrawler.network.connection.IPDeviceConnection;
import at.netcrawler.network.connection.snmp.SNMPObject.Type;


public abstract class SNMPConnection extends IPDeviceConnection implements
		SNMPManager {
	
	public static final int DEFAULT_MAX_REPETITIONS = 10;
	
	
	
	protected final SNMPConnectionSettings settings;
	protected final SNMPVersion version;
	
	
	public SNMPConnection(IPDeviceAccessor accessor,
			SNMPConnectionSettings settings) {
		super(accessor, settings);
		
		this.settings = new SNMPConnectionSettings(settings);
		version = settings.getVersion();
	}
	
	
	
	public SNMPConnectionSettings getSettings() {
		return settings;
	}
	
	@Override
	public SNMPVersion getVersion() {
		return version;
	}
	
	
	@Override
	public SNMPObject get(String oid) throws IOException {
		List<SNMPObject> result = get(new String[] {oid});
		if (result == null) return null;
		return result.get(0);
	}
	
	@Override
	public SNMPObject getNext(String oid) throws IOException { 
		List<SNMPObject> result = getNext(new String[] {oid});
		if (result == null) return null;
		return result.get(0);
	}
	
	@Override
	public List<SNMPObject> getBulk(String oid) throws IOException {
		return getBulk(DEFAULT_MAX_REPETITIONS, oid);
	}
	
	@Override
	public List<SNMPObject> getBulk(int maxRepetitions, String... oids) throws IOException {
		return getBulk(0, maxRepetitions, oids);
	}
	
	@Override
	public final List<SNMPObject> getBulk(int nonRepeaters, int maxRepetitions, String... oids) throws IOException {
		if (version == SNMPVersion.VERSION1)
			throw new UnsupportedOperationException("Version 1 doesn't support the GETBULK request");
		
		return getBulkImpl(nonRepeaters, maxRepetitions, oids);
	}
	
	protected abstract List<SNMPObject> getBulkImpl(int nonRepeaters, int maxRepetitions, String... oids) throws IOException;
	
	
	@Override
	public SNMPObject set(String oid, Type type, String value) throws IOException {
		return set(new SNMPObject(oid, type, value));
	}
	
	@Override
	public SNMPObject set(SNMPObject object) throws IOException {
		List<SNMPObject> result = set(new SNMPObject[] {object});
		if (result == null) return null;
		return result.get(0);
	}
	
	@Override
	public boolean setAndVerify(String oid, Type type, String value) throws IOException {
		return setAndVerify(new SNMPObject(oid, type, value));
	}
	
	@Override
	public boolean setAndVerify(SNMPObject... objects) throws IOException {
		List<SNMPObject> responses = set(objects);
		
		for (SNMPObject object : objects) {
			if (!responses.contains(object))
				return false;
		}
		
		return true;
	}
	
	
	@Override
	public List<SNMPObject> walkNext(String oid) throws IOException {
		List<SNMPObject> result = new ArrayList<SNMPObject>();
		String lastOid = oid;
		
		while (true) {
			SNMPObject nextObject = getNext(lastOid);
			String nextOid = nextObject.getOid();
			
			if (!nextOid.startsWith(oid))
				break;
			
			result.add(nextObject);
			lastOid = nextOid;
		}
		
		return result;
	}
	
	
	@Override
	public final List<SNMPObject> walkBulk(String oid) throws IOException {
		return walkBulk(DEFAULT_MAX_REPETITIONS, oid);
	}
	
	@Override
	public final List<SNMPObject> walkBulk(int maxRepetitions, String oid) throws IOException {
		if (version == SNMPVersion.VERSION1)
			throw new UnsupportedOperationException("Version 1 doesn't support the GETBULK request");
		
		return walkBulkImpl(maxRepetitions, oid);
	}
	
	protected List<SNMPObject> walkBulkImpl(int maxRepetitions, String oid) throws IOException {
		List<SNMPObject> result = new ArrayList<SNMPObject>();
		String lastOid = oid;
		
		mainLoop:
		while (true) {
			List<SNMPObject> nextBulk = getBulk(maxRepetitions, lastOid);
			String nextOid = nextBulk.get(nextBulk.size() - 1).getOid();
			
			if (!nextOid.startsWith(oid)) {
				for (int i = 0; i < nextBulk.size() - 1; i++) {
					SNMPObject object = nextBulk.get(i);
					
					if (!object.getOid().startsWith(oid))
						break mainLoop;
					
					result.add(object);
				}
			}
			
			result.addAll(nextBulk);
			lastOid = nextOid;
		}
		
		return result;
	}
	
	@Override
	public List<SNMPObject> walk(String oid) throws IOException {
		switch (version) {
		case VERSION1:
			return walkNext(oid);
		case VERSION2C:
		case VERSION3:
			return walkBulk(oid);
		}
		
		throw new IllegalStateException("Unimplemented version");
	}
	
	@Override
	public List<SNMPObject[]> walkNextTable(String... oids) throws IOException {
		List<SNMPObject[]> result = new ArrayList<SNMPObject[]>();
		
		int columns = oids.length;
		String[] lastOids = oids;
		
		mainLoop:
		while (true) {
			List<SNMPObject> nextObjects = getNext(lastOids);
			if (nextObjects.size() != columns)
				break;
			
			String[] nextOids = new String[columns];
			for (int i = 0; i < columns; i++) {
				String oid = nextObjects.get(i).getOid();
				if (!oid.startsWith(oids[i]))
					break mainLoop;
				
				nextOids[i] = oid;
			}
			
			result.add(nextObjects.toArray(new SNMPObject[columns]));
			lastOids = nextOids;
		}
		
		return result;
	}
	
	@Override
	public final List<SNMPObject[]> walkBulkTable(String... oids) throws IOException {
		return walkBulkTable(DEFAULT_MAX_REPETITIONS, oids);
	}
	
	@Override
	public final List<SNMPObject[]> walkBulkTable(int maxRepetitions, String... oids) throws IOException {
		if (version == SNMPVersion.VERSION1)
			throw new UnsupportedOperationException("Version 1 doesn't support the GETBULK request");
		
		return walkBulkTableImpl(maxRepetitions, oids);
	}
	
	protected List<SNMPObject[]> walkBulkTableImpl(int maxRepetitions, String... oids) throws IOException {
		List<SNMPObject[]> result = new ArrayList<SNMPObject[]>();
		
		int columns = oids.length;
		String[] lastOids = oids;
		
		mainLoop:
		while (true) {
			List<SNMPObject> nextBulk = getBulk(maxRepetitions, lastOids);
			String[] nextOids = new String[columns];
			
			int newRows = 0;
			for (int k = 0; k < nextBulk.size(); newRows++) {
				SNMPObject[] row = new SNMPObject[columns];
				
				for (int j = 0; j < columns; j++, k++) {
					SNMPObject object = nextBulk.get(k);
					String oid = object.getOid();
					
					if (!oid.startsWith(oids[j])) break mainLoop;
					
					row[j] = nextBulk.get(k);
				}
				
				result.add(row);
			}
			
			if (newRows < maxRepetitions) break;
			
			lastOids = nextOids;
		}
		
		return result;
	}
	
	@Override
	public List<SNMPObject[]> walkTable(String... oids) throws IOException {
		switch (version) {
		case VERSION1:
			return walkNextTable(oids);
		case VERSION2C:
		case VERSION3:
			return walkBulkTable(oids);
		}
		
		throw new IllegalStateException("Unimplemented version");
	}
	
}