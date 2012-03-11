package at.netcrawler.network.connection.snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.andiwand.library.util.ObjectIdentifier;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ConnectionType;
import at.netcrawler.network.connection.TCPIPDeviceConnection;


public abstract class SNMPConnection extends
		TCPIPDeviceConnection<SNMPSettings> implements SNMPManager {
	
	public static final int DEFAULT_MAX_REPETITIONS = 10;
	
	protected SNMPVersion version;
	
	public SNMPConnection(IPDeviceAccessor accessor, SNMPSettings settings) {
		super(accessor, settings);
		
		version = settings.getVersion();
	}
	
	@Override
	public final ConnectionType getConnectionType() {
		return ConnectionType.SNMP;
	}
	
	@Override
	public SNMPVersion getVersion() {
		return version;
	}
	
	@Override
	public SNMPEntry get(ObjectIdentifier oid) throws IOException {
		List<SNMPEntry> result = get(new ObjectIdentifier[] {oid});
		if (result == null) return null;
		return result.get(0);
	}
	
	@Override
	public SNMPEntry getNext(ObjectIdentifier oid) throws IOException {
		List<SNMPEntry> result = getNext(new ObjectIdentifier[] {oid});
		if (result == null) return null;
		return result.get(0);
	}
	
	@Override
	public List<SNMPEntry> getBulk(ObjectIdentifier oid) throws IOException {
		return getBulk(DEFAULT_MAX_REPETITIONS, oid);
	}
	
	@Override
	public List<SNMPEntry> getBulk(int maxRepetitions, ObjectIdentifier... oids)
			throws IOException {
		return getBulk(0, maxRepetitions, oids);
	}
	
	@Override
	public final List<SNMPEntry> getBulk(int nonRepeaters, int maxRepetitions,
			ObjectIdentifier... oids) throws IOException {
		if (version == SNMPVersion.VERSION1)
			throw new UnsupportedOperationException(
					"Version 1 doesn't support the GETBULK request!");
		
		return getBulkImpl(nonRepeaters, maxRepetitions, oids);
	}
	
	protected abstract List<SNMPEntry> getBulkImpl(int nonRepeaters,
			int maxRepetitions, ObjectIdentifier... oids) throws IOException;
	
	@Override
	public SNMPEntry set(ObjectIdentifier oid, Object value) throws IOException {
		return set(new SNMPEntry(oid, value));
	}
	
	@Override
	public SNMPEntry set(SNMPEntry entry) throws IOException {
		List<SNMPEntry> result = set(new SNMPEntry[] {entry});
		if (result == null) return null;
		return result.get(0);
	}
	
	@Override
	public boolean setAndVerify(ObjectIdentifier oid, Object value)
			throws IOException {
		return setAndVerify(new SNMPEntry(oid, value));
	}
	
	@Override
	public boolean setAndVerify(SNMPEntry... entries) throws IOException {
		List<SNMPEntry> responses = set(entries);
		
		for (SNMPEntry entry : entries) {
			if (!responses.contains(entry)) return false;
		}
		
		return true;
	}
	
	@Override
	public List<SNMPEntry> walkNext(ObjectIdentifier oid) throws IOException {
		List<SNMPEntry> result = new ArrayList<SNMPEntry>();
		ObjectIdentifier lastOid = oid;
		
		while (true) {
			SNMPEntry nextEntry = getNext(lastOid);
			ObjectIdentifier nextOid = nextEntry.getObjectIdentifier();
			
			if (!nextOid.startsWith(oid)) break;
			
			result.add(nextEntry);
			lastOid = nextOid;
		}
		
		return result;
	}
	
	@Override
	public final List<SNMPEntry> walkBulk(ObjectIdentifier oid)
			throws IOException {
		return walkBulk(DEFAULT_MAX_REPETITIONS, oid);
	}
	
	@Override
	public final List<SNMPEntry> walkBulk(int maxRepetitions,
			ObjectIdentifier oid) throws IOException {
		if (version == SNMPVersion.VERSION1)
			throw new UnsupportedOperationException(
					"Version 1 doesn't support the GETBULK request!");
		
		return walkBulkImpl(maxRepetitions, oid);
	}
	
	protected List<SNMPEntry> walkBulkImpl(int maxRepetitions,
			ObjectIdentifier oid) throws IOException {
		List<SNMPEntry> result = new ArrayList<SNMPEntry>();
		ObjectIdentifier lastOid = oid;
		
		mainLoop:
		while (true) {
			List<SNMPEntry> nextBulk = getBulk(maxRepetitions, lastOid);
			ObjectIdentifier nextOid = nextBulk.get(nextBulk.size() - 1)
					.getObjectIdentifier();
			
			if (!nextOid.startsWith(oid)) {
				for (int i = 0; i < nextBulk.size() - 1; i++) {
					SNMPEntry entry = nextBulk.get(i);
					
					if (!entry.getObjectIdentifier().startsWith(oid))
						break mainLoop;
					
					result.add(entry);
				}
			}
			
			result.addAll(nextBulk);
			lastOid = nextOid;
		}
		
		return result;
	}
	
	@Override
	public List<SNMPEntry> walk(ObjectIdentifier oid) throws IOException {
		switch (version) {
		case VERSION1:
			return walkNext(oid);
		case VERSION2C:
		case VERSION3:
			return walkBulk(oid);
		}
		
		throw new IllegalStateException("Unimplemented version!");
	}
	
	@Override
	public List<SNMPEntry[]> walkNextTable(ObjectIdentifier... oids)
			throws IOException {
		List<SNMPEntry[]> result = new ArrayList<SNMPEntry[]>();
		
		int columns = oids.length;
		ObjectIdentifier[] lastOids = oids;
		
		mainLoop:
		while (true) {
			List<SNMPEntry> nextEntries = getNext(lastOids);
			if (nextEntries.size() != columns) break;
			
			ObjectIdentifier[] nextOids = new ObjectIdentifier[columns];
			for (int i = 0; i < columns; i++) {
				ObjectIdentifier oid = nextEntries.get(i).getObjectIdentifier();
				if (!oid.startsWith(oids[i])) break mainLoop;
				
				nextOids[i] = oid;
			}
			
			result.add(nextEntries.toArray(new SNMPEntry[columns]));
			lastOids = nextOids;
		}
		
		return result;
	}
	
	@Override
	public final List<SNMPEntry[]> walkBulkTable(ObjectIdentifier... oids)
			throws IOException {
		return walkBulkTable(DEFAULT_MAX_REPETITIONS, oids);
	}
	
	@Override
	public final List<SNMPEntry[]> walkBulkTable(int maxRepetitions,
			ObjectIdentifier... oids) throws IOException {
		if (version == SNMPVersion.VERSION1)
			throw new UnsupportedOperationException(
					"Version 1 doesn't support the GETBULK request");
		
		return walkBulkTableImpl(maxRepetitions, oids);
	}
	
	protected List<SNMPEntry[]> walkBulkTableImpl(int maxRepetitions,
			ObjectIdentifier... oids) throws IOException {
		List<SNMPEntry[]> result = new ArrayList<SNMPEntry[]>();
		
		int columns = oids.length;
		ObjectIdentifier[] lastOids = oids;
		
		mainLoop:
		while (true) {
			List<SNMPEntry> nextBulk = getBulk(maxRepetitions, lastOids);
			ObjectIdentifier[] nextOids = new ObjectIdentifier[columns];
			
			int newRows = 0;
			for (int k = 0; k < nextBulk.size(); newRows++) {
				SNMPEntry[] row = new SNMPEntry[columns];
				
				for (int j = 0; j < columns; j++, k++) {
					SNMPEntry object = nextBulk.get(k);
					ObjectIdentifier oid = object.getObjectIdentifier();
					
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
	public List<SNMPEntry[]> walkTable(ObjectIdentifier... oids)
			throws IOException {
		switch (version) {
		case VERSION1:
			return walkNextTable(oids);
		case VERSION2C:
		case VERSION3:
			return walkBulkTable(oids);
		}
		
		throw new IllegalStateException("Unimplemented version!");
	}
	
}