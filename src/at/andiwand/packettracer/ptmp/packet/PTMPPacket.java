package at.andiwand.packettracer.ptmp.packet;

import java.util.Arrays;


public class PTMPPacket {
	
	public static final int TYPE_NEGOTIATION_REQUEST		= 0;
	public static final int TYPE_NEGOTIATION_RESPONSE		= 1;
	public static final int TYPE_AUTHENTICATION_REQUEST		= 2;
	public static final int TYPE_AUTHENTICATION_CHALLENGE	= 3;
	public static final int TYPE_AUTHENTICATION_RESPONSE	= 4;
	public static final int TYPE_AUTHENTICATION_STATUS		= 5;
	public static final int TYPE_KEEPALIVE					= 6;
	public static final int TYPE_DISCONNECT					= 7;
	public static final int TYPE_IPC_MESSAGES_MIN			= 100;
	public static final int TYPE_IPC_MESSAGES_MAX			= 199;
	public static final int TYPE_MULTIUSER_MESSAGES_MIN		= 200;
	public static final int TYPE_MULTIUSER_MESSAGES_MAX		= 299;
	
	
	private final int type;
	private final Object[] value;
	
	
	public PTMPPacket(int type, Object... value) {
		this.type = type;
		this.value = new Object[value.length];
		System.arraycopy(value, 0, this.value, 0, value.length);
	}
	
	
	@Override
	public String toString() {
		return "type: " + type + "; " + Arrays.toString(value);
	}
	
	
	public int getType() {
		return type;
	}
	public Object[] getValue() {
		Object[] result = new Object[value.length];
		System.arraycopy(value, 0, result, 0, value.length);
		
		return result;
	}
	
}