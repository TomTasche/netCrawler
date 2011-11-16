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
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
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

import at.netcrawler.network.IPDeviceAccessor;
import at.netcrawler.network.connection.snmp.SNMPObject.Type;


public class LocalSNMPConnection extends SNMPConnection {
	
	private static interface ValueConverter {
		public Variable convert(String value);
	}
	
	private static enum SupportedType implements ValueConverter {
		INTEGER(Type.INTEGER, Integer32.class) {
			public Variable convert(String value) {
				return new Integer32(Integer.parseInt(value));
			}
		},
		UNSIGNED(Type.UNSIGNED, UnsignedInteger32.class) {
			public Variable convert(String value) {
				return new UnsignedInteger32(Long.parseLong(value));
			}
		},
		STRING(Type.STRING, OctetString.class) {
			public Variable convert(String value) {
				return new OctetString(value);
			}
		},
		NULLOBJ(Type.NULLOBJ, Null.class) {
			public Variable convert(String value) {
				return new Null();
			}
		},
		OBJID(Type.OBJID, OID.class) {
			public Variable convert(String value) {
				return new OID(value);
			}
		},
		TIMETICKS(Type.TIMETICKS, TimeTicks.class) {
			public Variable convert(String value) {
				return new TimeTicks(Long.parseLong(value));
			}
		},
		IPADDRESS(Type.IPADDRESS, IpAddress.class) {
			public Variable convert(String value) {
				return new IpAddress(value);
			}
		};
		
		public static final Map<Class<? extends Variable>, Type> TYPE_MAP;
		public static final Map<Type, ValueConverter> VALUE_CONVERTER_MAP;
		
		static {
			Map<Class<? extends Variable>, Type> typeMap = new HashMap<Class<? extends Variable>, Type>();
			Map<Type, ValueConverter> valueConverterMap = new HashMap<Type, ValueConverter>();
			
			for (SupportedType type : values()) {
				typeMap.put(type.variableClass, type.type);
				valueConverterMap.put(type.type, type);
			}
			
			TYPE_MAP = Collections.unmodifiableMap(typeMap);
			VALUE_CONVERTER_MAP = Collections.unmodifiableMap(valueConverterMap);
		}
		
		public static Type getTypeByVariableClass(Class<? extends Variable> variableClass) {
			return TYPE_MAP.get(variableClass);
		}
		public static ValueConverter getValueConverterByType(Type type) {
			return VALUE_CONVERTER_MAP.get(type);
		}
		
		
		public final Type type;
		public final Class<? extends Variable> variableClass;
		
		private SupportedType(Type type, Class<? extends Variable> variableClass) {
			this.type = type;
			this.variableClass = variableClass;
		}
	}
	
	
	
	private static final String NO_PASSWORD		= "        ";
	private static final String NO_CRYPTO_KEY	= NO_PASSWORD;
	
	
	private static final Map<SNMPVersion, Integer> VERSION_TRANSLATION_MAP;
	private static final Map<SNMPSecurityLevel, Integer> SECURITY_LEVEL_TRANSLATION_MAP;
	
	
	static {
		Map<SNMPVersion, Integer> versionTranlationMap = new HashMap<SNMPVersion, Integer>();
		versionTranlationMap.put(SNMPVersion.VERSION1, SnmpConstants.version1);
		versionTranlationMap
				.put(SNMPVersion.VERSION2C, SnmpConstants.version2c);
		versionTranlationMap.put(SNMPVersion.VERSION3, SnmpConstants.version3);
		VERSION_TRANSLATION_MAP = Collections
				.unmodifiableMap(versionTranlationMap);
		
		Map<SNMPSecurityLevel, Integer> securityLevelTranlationMap = new HashMap<SNMPSecurityLevel, Integer>();
		securityLevelTranlationMap.put(SNMPSecurityLevel.NOAUTH_NOPRIV,
				SecurityLevel.NOAUTH_NOPRIV);
		securityLevelTranlationMap.put(SNMPSecurityLevel.AUTH_NOPRIV,
				SecurityLevel.AUTH_NOPRIV);
		securityLevelTranlationMap.put(SNMPSecurityLevel.AUTH_PRIV,
				SecurityLevel.AUTH_PRIV);
		SECURITY_LEVEL_TRANSLATION_MAP = Collections
				.unmodifiableMap(securityLevelTranlationMap);
	}
	
	
	
	private Snmp snmp;
	private Target target;
	
	
	public LocalSNMPConnection(IPDeviceAccessor accessor,
			SNMPConnectionSettings settings) throws IOException {
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
			USM usm = new USM(SecurityProtocols.getInstance(),
					new OctetString(MPv3.createLocalEngineID()), 0);
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
	public List<SNMPObject> get(String... oids) throws IOException {
		return buildSendAndConvert(PDU.GET, oids);
	}
	
	@Override
	public List<SNMPObject> getNext(String... oids) throws IOException {
		return buildSendAndConvert(PDU.GETNEXT, oids);
	}
	
	@Override
	protected List<SNMPObject> getBulkImpl(int nonRepeaters, int maxRepetitions, String... oids) throws IOException {
		return buildSendAndConvertBulk(nonRepeaters, maxRepetitions, oids);
	}
	
	@Override
	public List<SNMPObject> set(SNMPObject... objects) throws IOException {
		return buildSendAndConvertSet(objects);
	}
	
	
	private PDU build(int type) {
		PDU pdu = (version == SNMPVersion.VERSION3) ? new ScopedPDU()
				: new PDU();
		pdu.setType(type);
		
		return pdu;
	}
	private PDU build(int type, String... oids) {
		PDU pdu = build(type);
		
		for (String oid : oids) {
			pdu.add(new VariableBinding(new OID(oid)));
		}
		
		return pdu;
	}
	private PDU buildBulk(int nonRepeaters, int maxRepetitions, String... oids) {
		PDU pdu = build(PDU.GETBULK, oids);
		
		pdu.setNonRepeaters(nonRepeaters);
		pdu.setMaxRepetitions(maxRepetitions);
		
		return pdu;
	}
	private PDU buildSet(SNMPObject... objects) {
		PDU pdu = build(PDU.SET);
		
		for (SNMPObject object : objects) {
			String oidString = object.getOid();
			Type type = object.getType();
			String value = object.getValue();
			
			OID oid = new OID(oidString);
			Variable variable = SupportedType.getValueConverterByType(type).convert(value);
			
			pdu.add(new VariableBinding(oid, variable));
		}
		
		return pdu;
	}
	public PDU send(PDU pdu) throws IOException {
		return snmp.send(pdu, target).getResponse();
	}
	private PDU buildAndSend(int type, String... oids) throws IOException {
		PDU pdu = build(type, oids);
		
		return send(pdu);
	}
	private PDU buildAndSendBulk(int nonRepeaters, int maxRepetitions, String... oids) throws IOException {
		PDU pdu = buildBulk(nonRepeaters, maxRepetitions, oids);
		
		return send(pdu);
	}
	private PDU buildAndSendSet(SNMPObject... objects) throws IOException {
		PDU pdu = buildSet(objects);
		
		return send(pdu);
	}
	private List<SNMPObject> convert(PDU response) throws IOException {
		if (response == null)
			return null;
		
		List<SNMPObject> result = new ArrayList<SNMPObject>(response.size());
		
		for (int i = 0; i < response.size(); i++) {
			VariableBinding variableBinding = response.get(i);
			Variable variable = variableBinding.getVariable();
			
			String oid = variableBinding.getOid().toString();
			Type type = SupportedType.getTypeByVariableClass(variable.getClass());
			String value = variableBinding.getVariable().toString();
			
			result.add(new SNMPObject(oid, type, value));
		}
		
		return result;
	}
	private List<SNMPObject> buildSendAndConvert(int type, String... oids) throws IOException {
		PDU response = buildAndSend(type, oids);
		
		return convert(response);
	}
	private List<SNMPObject> buildSendAndConvertBulk(int nonRepeaters, int maxRepetitions, String... oids) throws IOException {
		PDU response = buildAndSendBulk(nonRepeaters, maxRepetitions, oids);
		
		return convert(response);
	}
	private List<SNMPObject> buildSendAndConvertSet(SNMPObject... objects) throws IOException {
		PDU response = buildAndSendSet(objects);
		
		return convert(response);
	}
	
	@Override
	protected void closeImpl() throws IOException {
		snmp.close();
	}
	
}