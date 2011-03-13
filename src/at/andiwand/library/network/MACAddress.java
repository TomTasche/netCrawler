package at.andiwand.library.network;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Random;


public class MACAddress {
	
	public static final int SIZE = 6;
	
	public static final SimpleMACAddressFormat DEFAULT_FORMAT =
		new SimpleMACAddressFormat();
	
	public static final MACAddress BROADCAST_ADDRESS =
		new MACAddress(0xff, 0xff, 0xff, 0xff, 0xff, 0xff);
	
	
	
	
	private final byte[] address = new byte[SIZE];
	
	
	
	public MACAddress(byte... address) {
		if (address.length != SIZE)
			throw new IllegalArgumentException("address has a illegal length!");
		
		System.arraycopy(address, 0, this.address, 0, SIZE);
	}
	public MACAddress(long address) {
		for (int i = 0; i < SIZE; i++) {
			this.address[i] = (byte) (address >> ((SIZE - 1 - i) * 8));
		}
	}
	public MACAddress(int... address) {
		if (address.length != SIZE)
			throw new IllegalArgumentException("address has a illegal length!");
		
		for (int i = 0; i < SIZE; i++) {
			this.address[i] = (byte) address[i];
		}
	}
	public MACAddress(String address) {
		try {
			MACAddress thisAddress = DEFAULT_FORMAT.parseObject(address);
			
			System.arraycopy(thisAddress.address, 0, this.address, 0, SIZE);
		} catch (ParseException e) {
			throw new IllegalArgumentException("address has a illegal format!");
		}
	}
	
	
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(address);
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof MACAddress)) return false;
		MACAddress address = (MACAddress) obj;
		
		return Arrays.equals(this.address, address.address);
	}
	public String toString() {
		return DEFAULT_FORMAT.format(this);
	}
	
	
	public byte[] getAddress() {
		byte[] result = new byte[SIZE];
		System.arraycopy(address, 0, result, 0, SIZE);
		
		return result;
	}
	
	public boolean isBroadcast() {
		return equals(BROADCAST_ADDRESS);
	}
	
	
	public long toLong() {
		long result = 0;
		
		for (int i = 0; i < SIZE; i++) {
			result |= (long) (address[i] & 0xff) << ((SIZE - 1 - i) * 8);
		}
		
		return result;
	}
	
	
	public static MACAddress randomAddress() {
		byte[] address = new byte[SIZE];
		Random random = new Random();
		
		for (int i = 1; i < SIZE; i++) {
			address[i] = (byte) random.nextInt();
		}
		
		return new MACAddress(address);
	}
	
}