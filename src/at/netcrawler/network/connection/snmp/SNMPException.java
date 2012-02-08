package at.netcrawler.network.connection.snmp;

import java.io.IOException;


public class SNMPException extends IOException {
	
	private static final long serialVersionUID = 7276601205347277881L;
	
	public SNMPException() {}
	
	public SNMPException(String message) {
		super(message);
	}
	
	public SNMPException(Throwable cause) {
		super(cause);
	}
	
	public SNMPException(String message, Throwable cause) {
		super(message, cause);
	}
	
}