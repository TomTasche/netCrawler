package at.andiwand.packettracer.ptmp;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.nio.charset.Charset;
import java.util.UUID;

import at.andiwand.library.util.UUIDUtil;




//TODO: implement encryption
//TODO: implement compression
public class PTMPDataOutputStream extends FilterOutputStream implements DataOutput {
	
	public static final Charset CHARSET = Charset.forName("utf-8");
	public static final Charset BYTES_CHARSET = Charset.forName("us-ascii");
	public static final Charset CHARS_CHARSET = Charset.forName("utf-8");
	
	
	
	private PTMPConfiguration configuration;
	
	private DataOutputStream dataOutputStream;
	
	
	
	public PTMPDataOutputStream(OutputStream out) {
		this(out, PTMPConfiguration.DEFAULT);
	}
	public PTMPDataOutputStream(OutputStream out, PTMPConfiguration configuration) {
		super(new DataOutputStream(out));
		
		setConfiguration(configuration);
		
		dataOutputStream = (DataOutputStream) this.out;
	}
	
	
	
	public PTMPConfiguration getConfiguration() {
		return configuration;
	}
	
	public int streamedBytes() {
		return dataOutputStream.size();
	}
	
	
	public void setConfiguration(PTMPConfiguration configuration) {
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
	public void writeBoolean(boolean b) throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String boolString = b + "";
			
			writeString(boolString);
		} else {
			dataOutputStream.writeBoolean(b);
		}
	}
	
	@Override
	public void writeByte(int b) throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String byteString = b + "";
			
			writeString(byteString);
		} else {
			dataOutputStream.writeByte(b);
		}
	}
	
	@Override
	public void writeChar(int c) throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String charString = c + "";
			
			writeString(charString);
		} else {
			dataOutputStream.writeChar(c);
		}
	}
	
	@Override
	public void writeShort(int s) throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String shortString = s + "";
			
			writeString(shortString);
		} else {
			dataOutputStream.writeShort(s);
		}
	}
	
	@Override
	public void writeInt(int i) throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String intString = i + "";
			
			writeString(intString);
		} else {
			dataOutputStream.writeInt(i);
		}
	}
	
	@Override
	public void writeLong(long l) throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String longString = l + "";
			
			writeString(longString);
		} else {
			dataOutputStream.writeLong(l);
		}
	}
	
	@Override
	public void writeFloat(float f) throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String floatString = f + "";
			
			writeString(floatString);
		} else {
			dataOutputStream.writeFloat(f);
		}
	}
	
	@Override
	public void writeDouble(double d) throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String doubleString = d + "";
			
			writeString(doubleString);
		} else {
			dataOutputStream.writeDouble(d);
		}
	}
	
	public void writeString(String string) throws IOException {
		out.write(string.getBytes(CHARSET));
		out.write("\0".getBytes(CHARSET));
	}
	
	@Override
	public void writeUTF(String s) throws IOException {
		writeString(s);
	}
	
	@Override
	public void writeBytes(String s) throws IOException {
		out.write(s.getBytes(BYTES_CHARSET));
		out.write("\0".getBytes(BYTES_CHARSET));
	}
	
	@Override
	public void writeChars(String s) throws IOException {
		out.write(s.getBytes(CHARS_CHARSET));
		out.write("\0".getBytes(CHARS_CHARSET));
	}
	
	
	public void writeIp4Addres(Inet4Address ip4) throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String ip4String = ip4.getHostAddress();
			
			writeString(ip4String);
		} else {
			out.write(ip4.getAddress());
		}
	}
	
	public void writeIp6Addres(Inet6Address ip6) throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String ip6String = ip6.getHostAddress();
			
			writeString(ip6String);
		} else {
			out.write(ip6.getAddress());
		}
	}
	
	public void writeUuid(UUID uuid) throws IOException {
		if (configuration.encoding() == PTMPConfiguration.ENCODING_TEXT) {
			String uuidString = "{" + uuid.toString() + "}";
			
			writeString(uuidString);
		} else if (configuration.encoding() == PTMPConfiguration.ENCODING_BINARY) {
			byte[] uuidBytes = UUIDUtil.uuidToBytes(uuid);
			
			write(uuidBytes);
		}
	}
	
	
	public void writeObject(Object object) throws IOException {
		if (object.getClass().equals(Boolean.class)) {
			writeBoolean((Boolean) object);
		} else if (object.getClass().equals(Byte.class)) {
			writeByte((Byte) object);
		} else if (object.getClass().equals(Character.class)) {
			writeChar((Character) object);
		} else if (object.getClass().equals(Short.class)) {
			writeShort((Short) object);
		} else if (object.getClass().equals(Integer.class)) {
			writeInt((Integer) object);
		} else if (object.getClass().equals(Long.class)) {
			writeLong((Long) object);
		} else if (object.getClass().equals(Float.class)) {
			writeFloat((Float) object);
		} else if (object.getClass().equals(Double.class)) {
			writeDouble((Double) object);
		} else if (object.getClass().equals(String.class)) {
			writeString((String) object);
		} else if (object.getClass().equals(Inet4Address.class)) {
			writeIp4Addres((Inet4Address) object);
		} else if (object.getClass().equals(Inet6Address.class)) {
			writeIp6Addres((Inet6Address) object);
		} else if (object.getClass().equals(UUID.class)) {
			writeUuid((UUID) object);
		} else if (object.getClass().equals(byte[].class)) {
			write((byte[]) object);
		} else {
			throw new IllegalArgumentException("unsupported argument is given!");
		}
	}
	
	
	public void writeData(Object... data) throws IOException {
		for (int i = 0; i < data.length; i++) {
			writeObject(data[i]);
		}
	}
	
	
	
	@Override
	public void flush() throws IOException {
		out.flush();
	}
	
}