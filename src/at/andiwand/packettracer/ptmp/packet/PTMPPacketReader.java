package at.andiwand.packettracer.ptmp.packet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import at.andiwand.packettracer.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.ptmp.PTMPDataInputStream;




// TODO: implement encryption
public class PTMPPacketReader {
	
	public static final Charset CHARSET = Charset.forName("utf-8");
	
	
	private InputStream inputStream;
	private PTMPDataInputStream dataInputStream;
	
	private Map<Integer, Class<?>[]> definitions;
	
	
	public PTMPPacketReader(InputStream inputStream) {
		this(inputStream, PTMPConfiguration.DEFAULT);
	}
	public PTMPPacketReader(InputStream inputStream, PTMPConfiguration configuration) {
		this.inputStream = inputStream;
		
		dataInputStream = new PTMPDataInputStream(inputStream);
		dataInputStream.setConfiguration(configuration);
		
		definitions = new HashMap<Integer, Class<?>[]>();
		addPacketDefinition(0, String.class, Integer.class, UUID.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class, Integer.class, String.class);
		addPacketDefinition(1, String.class, Integer.class, UUID.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class, Integer.class, String.class);
		addPacketDefinition(2, String.class);
		addPacketDefinition(3, String.class);
		addPacketDefinition(4, String.class);
		addPacketDefinition(5, Boolean.class);
	}
	
	
	public PTMPConfiguration getConfiguration() {
		return dataInputStream.getConfiguration();
	}
	
	public void setConfiguration(PTMPConfiguration configuration) {
		dataInputStream.setConfiguration(configuration);
	}
	
	
	public void addPacketDefinition(int type, Class<?>... definition) {
		definitions.put(type, definition);
	}
	public void removePacketDefinition(int type) {
		definitions.remove(type);
	}
	
	
	public PTMPPacket readPacket() throws IOException {
		int length = dataInputStream.readInt();
		
		int typeStartByte = dataInputStream.streamedBytes();
		int type = dataInputStream.readInt();
		
		Class<?>[] definition = definitions.get(type);
		if (definition == null) throw new RuntimeException("unknown type!");
		
		int typeBytes = dataInputStream.streamedBytes() - typeStartByte;
		
		byte[] buffer = new byte[length - typeBytes];
		inputStream.read(buffer);
		
		
		Object[] value = new Object[definition.length + 1];
		ByteArrayInputStream bufferInputStream = new ByteArrayInputStream(buffer);
		PTMPDataInputStream valueInputStream = new PTMPDataInputStream(bufferInputStream);
		valueInputStream.setConfiguration(getConfiguration());
		valueInputStream.readData(value, definition);
		
		byte[] data = new byte[buffer.length - valueInputStream.streamedBytes()];
		valueInputStream.read(data);
		value[definition.length] = data;
		
		return new PTMPPacket(type, value);
	}
	
}