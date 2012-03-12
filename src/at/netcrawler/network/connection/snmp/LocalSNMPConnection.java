package at.netcrawler.network.connection.snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.util.ObjectIdentifier;
import at.andiwand.library.util.Timeticks;
import at.netcrawler.network.accessor.IPDeviceAccessor;


public class LocalSNMPConnection extends SNMPConnection {
	
	private static interface ValueConverter {
		public Variable convertFromObject(Object value);
		
		public Object convertFromVariable(Variable value);
	}
	
	// TODO: improve
	private static enum SupportedType implements ValueConverter {
		INTEGER(SNMPObjectType.INTEGER, Integer32.class) {
			public Variable convertFromObject(Object value) {
				return new Integer32((Integer) value);
			}
			
			public Object convertFromVariable(Variable value) {
				return ((Integer32) value).getValue();
			}
		},
		UNSIGNED(SNMPObjectType.UNSIGNED, UnsignedInteger32.class) {
			public Variable convertFromObject(Object value) {
				return new UnsignedInteger32((Long) value);
			}
			
			public Object convertFromVariable(Variable value) {
				return ((UnsignedInteger32) value).getValue();
			}
		},
		COUNTER32(SNMPObjectType.INTEGER, Counter32.class) {
			public Variable convertFromObject(Object value) {
				return new Counter32((Long) value);
			}
			
			public Object convertFromVariable(Variable value) {
				return ((Counter32) value).getValue();
			}
		},
		COUNTER64(SNMPObjectType.INTEGER, Counter64.class) {
			public Variable convertFromObject(Object value) {
				return new Counter64((Long) value);
			}
			
			public Object convertFromVariable(Variable value) {
				return ((Counter64) value).getValue();
			}
		},
		STRING(SNMPObjectType.STRING, OctetString.class) {
			public Variable convertFromObject(Object value) {
				return new OctetString((String) value);
			}
			
			public Object convertFromVariable(Variable value) {
				return ((OctetString) value).toString();
			}
		},
		OBJID(SNMPObjectType.OBJID, OID.class) {
			public Variable convertFromObject(Object value) {
				return new OID(((ObjectIdentifier) value).getValue());
			}
			
			public Object convertFromVariable(Variable value) {
				return new ObjectIdentifier(((OID) value).getValue());
			}
		},
		NULL_OBJ(SNMPObjectType.NULL_OBJ, Null.class) {
			public Variable convertFromObject(Object value) {
				return new Null(((SNMPNull) value).getSyntax());
			}
			
			public Object convertFromVariable(Variable value) {
				Null nvll = (Null) value;
				return new SNMPNull(nvll.getSyntax(), nvll.getSyntaxString());
			}
		},
		TIMETICKS(SNMPObjectType.TIMETICKS, TimeTicks.class) {
			@Override
			public Variable convertFromObject(Object value) {
				return new TimeTicks(((Timeticks) value).getMillis());
			}
			
			@Override
			public Object convertFromVariable(Variable value) {
				return new Timeticks(((TimeTicks) value).getValue());
			}
		},
		IPADDRESS(SNMPObjectType.IPADDRESS, IpAddress.class) {
			public Variable convertFromObject(Object value) {
				return new IpAddress(((IPv4Address) value).toByteArray());
			}
			
			public Object convertFromVariable(Variable value) {
				return new IPv4Address(((IpAddress) value).toByteArray());
			}
		};
		
		private static final Map<Class<? extends Variable>, ValueConverter> VARIABLE_VALUE_CONVERTER_MAP;
		private static final Map<SNMPObjectType, ValueConverter> OBJECT_VALUE_CONVERTER_MAP;
		
		static {
			Map<Class<? extends Variable>, ValueConverter> variableValueConverterMap = new HashMap<Class<? extends Variable>, ValueConverter>();
			Map<SNMPObjectType, ValueConverter> objectValueConverterMap = new HashMap<SNMPObjectType, ValueConverter>();
			
			for (SupportedType type : values()) {
				variableValueConverterMap.put(type.variableClass, type);
				objectValueConverterMap.put(type.type, type);
			}
			
			VARIABLE_VALUE_CONVERTER_MAP = Collections
					.unmodifiableMap(variableValueConverterMap);
			OBJECT_VALUE_CONVERTER_MAP = Collections
					.unmodifiableMap(objectValueConverterMap);
		}
		
		public static ValueConverter getConverterByVariableClass(
				Class<? extends Variable> variableClass) {
			return VARIABLE_VALUE_CONVERTER_MAP.get(variableClass);
		}
		
		public static ValueConverter getConverterByType(SNMPObjectType type) {
			return OBJECT_VALUE_CONVERTER_MAP.get(type);
		}
		
		private final SNMPObjectType type;
		private final Class<? extends Variable> variableClass;
		
		private SupportedType(SNMPObjectType type,
				Class<? extends Variable> variableClass) {
			this.type = type;
			this.variableClass = variableClass;
		}
	}
	
	private static final String NO_PASSWORD = "        ";
	private static final String NO_CRYPTO_KEY = NO_PASSWORD;
	
	private static final Map<SNMPVersion, Integer> VERSION_TRANSLATION_MAP = new HashMap<SNMPVersion, Integer>() {
		private static final long serialVersionUID = 1051440945943102967L;
		
		{
			put(SNMPVersion.VERSION1, SnmpConstants.version1);
			put(SNMPVersion.VERSION2C, SnmpConstants.version2c);
			put(SNMPVersion.VERSION3, SnmpConstants.version3);
		}
	};
	private static final Map<SNMPSecurityLevel, Integer> SECURITY_LEVEL_TRANSLATION_MAP = new HashMap<SNMPSecurityLevel, Integer>() {
		private static final long serialVersionUID = 3860939352265705050L;
		
		{
			put(SNMPSecurityLevel.NOAUTH_NOPRIV, SecurityLevel.NOAUTH_NOPRIV);
			put(SNMPSecurityLevel.AUTH_NOPRIV, SecurityLevel.AUTH_NOPRIV);
			put(SNMPSecurityLevel.AUTH_PRIV, SecurityLevel.AUTH_PRIV);
		}
	};
	
	private Snmp snmp;
	private Target target;
	
	public LocalSNMPConnection(IPDeviceAccessor accessor, SNMPSettings settings)
			throws IOException {
		super(accessor, settings);
		
		DefaultUdpTransportMapping transportMapping = new DefaultUdpTransportMapping();
		snmp = new Snmp(transportMapping);
		transportMapping.listen();
		
		switch (version) {
		case VERSION1:
		case VERSION2C:
			CommunityTarget communityTarget = new CommunityTarget();
			communityTarget.setCommunity(new OctetString(settings
					.getCommunity()));
			target = communityTarget;
			break;
		case VERSION3:
			USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(
					MPv3.createLocalEngineID()), 0);
			SecurityModels.getInstance().addSecurityModel(usm);
			
			String username = settings.getUsername();
			String password = settings.getPassword();
			String cryptoKey = settings.getCryptoKey();
			OctetString usernameOct = new OctetString(username);
			OctetString passwordOct = new OctetString(
					(password == null) ? NO_PASSWORD : password);
			OctetString cryptoKeyOct = new OctetString(
					(cryptoKey == null) ? NO_CRYPTO_KEY : cryptoKey);
			
			UsmUser usmUser = new UsmUser(usernameOct, AuthMD5.ID, passwordOct,
					PrivDES.ID, cryptoKeyOct);
			usm.addUser(usernameOct, usmUser);
			
			UserTarget userTarget = new UserTarget();
			userTarget.setSecurityName(usernameOct);
			userTarget.setSecurityModel(SECURITY_LEVEL_TRANSLATION_MAP
					.get(settings.getSecurityLevel()));
			target = userTarget;
			break;
		}
		
		target.setAddress(GenericAddress.parse("udp:" + accessor.getIpAddress()
				+ "/" + settings.getPort()));
		target.setVersion(VERSION_TRANSLATION_MAP.get(version));
		target.setRetries(settings.getRetries());
		target.setTimeout(settings.getTimeout());
	}
	
	@Override
	protected void closeImpl() throws IOException {
		snmp.close();
	}
	
	@Override
	public List<SNMPEntry> get(ObjectIdentifier... oids) throws IOException {
		return buildSendAndConvert(PDU.GET, oids);
	}
	
	@Override
	public List<SNMPEntry> getNext(ObjectIdentifier... oids) throws IOException {
		return buildSendAndConvert(PDU.GETNEXT, oids);
	}
	
	@Override
	protected List<SNMPEntry> getBulkImpl(int nonRepeaters, int maxRepetitions,
			ObjectIdentifier... oids) throws IOException {
		return buildSendAndConvertBulk(nonRepeaters, maxRepetitions, oids);
	}
	
	@Override
	public List<SNMPEntry> set(SNMPEntry... entries) throws IOException {
		return buildSendAndConvertSet(entries);
	}
	
	private PDU build(int type) {
		PDU pdu = (version == SNMPVersion.VERSION3) ? new ScopedPDU()
				: new PDU();
		pdu.setType(type);
		
		return pdu;
	}
	
	private PDU build(int type, ObjectIdentifier... oids) {
		PDU pdu = build(type);
		
		for (ObjectIdentifier oid : oids) {
			pdu.add(new VariableBinding(new OID(oid.getValue())));
		}
		
		return pdu;
	}
	
	private PDU buildBulk(int nonRepeaters, int maxRepetitions,
			ObjectIdentifier... oids) {
		PDU pdu = build(PDU.GETBULK, oids);
		
		pdu.setNonRepeaters(nonRepeaters);
		pdu.setMaxRepetitions(maxRepetitions);
		
		return pdu;
	}
	
	private PDU buildSet(SNMPEntry... entries) {
		PDU pdu = build(PDU.SET);
		
		for (SNMPEntry entry : entries) {
			ObjectIdentifier objectIdentifier = entry.getObjectIdentifier();
			SNMPObjectType type = entry.getType();
			Object value = entry.getValue();
			
			OID oid = new OID(objectIdentifier.getValue());
			ValueConverter valueConverter = SupportedType
					.getConverterByType(type);
			Variable variable = valueConverter.convertFromObject(value);
			
			pdu.add(new VariableBinding(oid, variable));
		}
		
		return pdu;
	}
	
	public PDU send(PDU pdu) throws IOException {
		ResponseEvent responseEvent = snmp.send(pdu, target);
		PDU response = responseEvent.getResponse();
		if (response == null) throw new IOException("Agent unreachable!");
		return response;
	}
	
	private PDU buildAndSend(int type, ObjectIdentifier... oids)
			throws IOException {
		PDU pdu = build(type, oids);
		
		return send(pdu);
	}
	
	private PDU buildAndSendBulk(int nonRepeaters, int maxRepetitions,
			ObjectIdentifier... oids) throws IOException {
		PDU pdu = buildBulk(nonRepeaters, maxRepetitions, oids);
		
		return send(pdu);
	}
	
	private PDU buildAndSendSet(SNMPEntry... entries) throws IOException {
		PDU pdu = buildSet(entries);
		
		return send(pdu);
	}
	
	private List<SNMPEntry> convert(PDU response) throws IOException {
		List<SNMPEntry> result = new ArrayList<SNMPEntry>(response.size());
		
		for (int i = 0; i < response.size(); i++) {
			VariableBinding variableBinding = response.get(i);
			Variable variable = variableBinding.getVariable();
			
			ObjectIdentifier oid = new ObjectIdentifier(variableBinding
					.getOid().toString());
			ValueConverter valueConverter = SupportedType
					.getConverterByVariableClass(variable.getClass());
			Object value = valueConverter.convertFromVariable(variable);
			
			result.add(new SNMPEntry(oid, value));
		}
		
		return result;
	}
	
	private List<SNMPEntry> buildSendAndConvert(int type,
			ObjectIdentifier... oids) throws IOException {
		PDU response = buildAndSend(type, oids);
		
		return convert(response);
	}
	
	private List<SNMPEntry> buildSendAndConvertBulk(int nonRepeaters,
			int maxRepetitions, ObjectIdentifier... oids) throws IOException {
		PDU response = buildAndSendBulk(nonRepeaters, maxRepetitions, oids);
		
		return convert(response);
	}
	
	private List<SNMPEntry> buildSendAndConvertSet(SNMPEntry... objects)
			throws IOException {
		PDU response = buildAndSendSet(objects);
		
		return convert(response);
	}
	
}