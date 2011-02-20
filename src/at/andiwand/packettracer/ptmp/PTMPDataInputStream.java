package at.andiwand.packettracer.ptmp;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.nio.charset.Charset;
import java.util.UUID;

import at.andiwand.library.util.UUIDUtil;
import at.andiwand.library.util.stream.CountingInputStream;



//TODO: implement encryption
//TODO: implement compression
public class PTMPDataInputStream extends FilterInputStream implements DataInput {
	
	public static final int BYTE_SIZE = 1;
	public static final int BOOL_SIZE = 1;
	public static final int INT_SIZE = 4;
	public static final String STRING_TERMINATION = "\0";
	public static final Charset STRING_CHARSET = Charset.forName("utf-8");
	public static final int IP4_SIZE = 4;
	public static final int IP6_SIZE = 16;
	public static final int UUID_SIZE = UUIDUtil.UUID_SIZE;
	
	
	public static final int BUFFER_SIZE = 1000;
	
	
	
	private PTMPConfiguration configuration;
	
	private CountingInputStream countingInputStream;
	private DataInputStream dataInputStream;
	
	
	
	public PTMPDataInputStream(InputStream inputStream) {
		this(inputStream, PTMPConfiguration.DEFAULT);
	}
	public PTMPDataInputStream(InputStream in, PTMPConfiguration configuration) {
		super(new CountingInputStream(in));
		
		setConfiguration(configuration);
		
		countingInputStream = (CountingInputStream) this.in;
		dataInputStream = new DataInputStream(countingInputStream);
	}
	
	
	
	public PTMPConfiguration getConfiguration() {
		return configuration;
	}
	
	public int streamedBytes() {
		return countingInputStream.count();
	}
	
	
	public synchronized void setConfiguration(PTMPConfiguration configuration) {
		if (configuration.encryption() != PTMPConfiguration.ENCRYPTION_NONE)
			throw new RuntimeException("encryption is not supported so far!");
		if ((configuration.encoding() != PTMPConfiguration.ENCODING_TEXT) &&
				(configuration.encoding() != PTMPConfiguration.ENCODING_BINARY))
			throw new RuntimeException("unknown encoding type " + configuration.encoding());
		if (configuration.compression() != PTMPConfiguration.COMPRESSION_NO)
			throw new RuntimeException("compression is not supported so far!");
		
		this.configuration = configuration;
	}
	
	
	
	@Override
	public boolean readBoolean() throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String boolString = readString();
			
			return Boolean.parseBoolean(boolString);
		} else {
			return dataInputStream.readBoolean();
		}
	}
	
	@Override
	public byte readByte() throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String byteString = readString();
			
			return Byte.parseByte(byteString);
		} else {
			return dataInputStream.readByte();
		}
	}
	
	@Override
	public int readUnsignedByte() throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String ubyteString = readString();
			
			return Integer.parseInt(ubyteString);
		} else {
			return dataInputStream.readUnsignedByte();
		}
	}
	
	@Override
	public char readChar() throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String charString = readString();
			
			return charString.charAt(0);
		} else {
			return dataInputStream.readChar();
		}
	}
	
	@Override
	public short readShort() throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String shortString = readString();
			
			return Short.parseShort(shortString);
		} else {
			return dataInputStream.readShort();
		}
	}
	
	@Override
	public int readUnsignedShort() throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String ushortString = readString();
			
			return Integer.parseInt(ushortString);
		} else {
			return dataInputStream.readUnsignedShort();
		}
	}
	
	@Override
	public int readInt() throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String intString = readString();
			
			return Integer.parseInt(intString);
		} else {
			return dataInputStream.readInt();
		}
	}
	
	@Override
	public long readLong() throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String longString = readString();
			
			return Long.parseLong(longString);
		} else {
			return dataInputStream.readLong();
		}
	}
	
	@Override
	public float readFloat() throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String floatString = readString();
			
			return Float.parseFloat(floatString);
		} else {
			return dataInputStream.readFloat();
		}
	}
	
	@Override
	public double readDouble() throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String doubleString = readString();
			
			return Double.parseDouble(doubleString);
		} else {
			return dataInputStream.readDouble();
		}
	}
	
	public String readString() throws IOException {
		StringBuilder builder = new StringBuilder();
		int read;
		
		while (true) {
			read = in.read();
			if (read == -1) throw new EOFException();
			if (read == '\0') break;
			builder.appendCodePoint(read);
		}
		
		return builder.toString();
	}
	
	@Override
	@Deprecated
	public String readLine() throws IOException {
		return dataInputStream.readLine();
	}
	
	@Override
	public String readUTF() throws IOException {
		return readString();
	}
	
	
	public Inet4Address readIp4Addres() throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String ipString = readString();
			
			return (Inet4Address) Inet4Address.getByName(ipString);
		} else {
			byte[] ip4bytes = new byte[IP4_SIZE];
			read(ip4bytes);
			
			return (Inet4Address) Inet4Address.getByAddress(ip4bytes);
		}
	}
	
	public Inet6Address readIp6Addres() throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String ipString = readString();
			
			return (Inet6Address) Inet6Address.getByName(ipString);
		} else {
			byte[] ip6bytes = new byte[IP6_SIZE];
			read(ip6bytes);
			
			return (Inet6Address) Inet6Address.getByAddress(ip6bytes);
		}
	}
	
	public UUID readUuid() throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String uuidString = readString();
			uuidString = uuidString.substring(1, uuidString.length() - 1);
			
			return UUID.fromString(uuidString);
		} else {
			byte[] uuidBytes = new byte[UUID_SIZE];
			read(uuidBytes);
			
			return UUIDUtil.bytesToUuid(uuidBytes);
		}
	}
	
	
	public Object readObject(Class<?> clazz) throws IOException {
		if (clazz.equals(Boolean.class)) {
			return readBoolean();
		} else if (clazz.equals(Byte.class)) {
			return readByte();
		} else if (clazz.equals(Character.class)) {
			return readChar();
		} else if (clazz.equals(Short.class)) {
			return readShort();
		} else if (clazz.equals(Integer.class)) {
			return readInt();
		} else if (clazz.equals(Long.class)) {
			return readLong();
		} else if (clazz.equals(Float.class)) {
			return readFloat();
		} else if (clazz.equals(Double.class)) {
			return readDouble();
		} else if (clazz.equals(String.class)) {
			return readString();
		} else if (clazz.equals(Inet4Address.class)) {
			return readIp4Addres();
		} else if (clazz.equals(Inet6Address.class)) {
			return readIp6Addres();
		} else if (clazz.equals(UUID.class)) {
			return readUuid();
		} else {
			throw new IllegalArgumentException("unsupported argument is given!");
		}
	}
	
	
	public Object[] readData(Class<?>... definition) throws IOException {
		Object[] result = new Object[definition.length];
		
		for (int i = 0; i < definition.length; i++) {
			result[i] = readObject(definition[i]);
		}
		
		return result;
	}
	
	public void readData(Object[] data, Class<?>... definition) throws IOException {
		for (int i = 0; i < definition.length; i++) {
			data[i] = readObject(definition[i]);
		}
	}
	
	
	
	@Override
	public void readFully(byte[] b) throws IOException {
		dataInputStream.readFully(b);
	}
	
	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		dataInputStream.read(b, off, len);
	}
	
	
	@Override
	public int skipBytes(int n) throws IOException {
		return dataInputStream.skipBytes(n);
	}
	
}