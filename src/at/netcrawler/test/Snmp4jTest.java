package at.netcrawler.test;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;


public class Snmp4jTest {
	
	public static void main(String[] args) throws Throwable {
		String address = "udp:192.168.0.161/161";
		String community = "netCrawler";
		
		DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
		Snmp snmp = new Snmp(transport);
		transport.listen();
		
		CommunityTarget target = new CommunityTarget();
		target.setAddress(GenericAddress.parse(address));
		target.setCommunity(new OctetString(community));
		target.setRetries(2);
		target.setTimeout(1500);
		target.setVersion(SnmpConstants.version2c);
		
		PDU pdu = new PDU();
		pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1")));
		pdu.setType(PDU.GETBULK);
		pdu.setMaxRepetitions(100);
		pdu.setNonRepeaters(0);
		
		ResponseEvent responseEvent = snmp.send(pdu, target);
		PDU response = responseEvent.getResponse();
		System.out.println(response.size());
		
		for (int i = 0; i < response.size(); i++) {
			VariableBinding variableBinding = response.get(i);
			System.out.println(variableBinding);
		}
	}
	
}