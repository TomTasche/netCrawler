package at.andiwand.packettracer.ptmp.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import at.andiwand.packettracer.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.ptmp.PTMPDataOutputStream;




public class PTMPPacketWriter {
	
	public static final Charset CHARSET = Charset.forName("utf-8");
	
	
	private PTMPDataOutputStream dataOutputStream;
	
	
	public PTMPPacketWriter(OutputStream outputStream) {
		this(outputStream, PTMPConfiguration.DEFAULT);
	}
	public PTMPPacketWriter(OutputStream outputStream, PTMPConfiguration configuration) {
		dataOutputStream = new PTMPDataOutputStream(outputStream);
		dataOutputStream.setConfiguration(configuration);
	}
	
	
	public PTMPConfiguration getConfiguration() {
		return dataOutputStream.getConfiguration();
	}
	
	public void setConfiguration(PTMPConfiguration configuration) {
		dataOutputStream.setConfiguration(configuration);
	}
	
	
	public void writePacket(PTMPPacket packet) throws IOException {
		ByteArrayOutputStream bufferOutputStream = new ByteArrayOutputStream();
		PTMPDataOutputStream packetOutputStream = new PTMPDataOutputStream(bufferOutputStream);
		packetOutputStream.setConfiguration(getConfiguration());
		packetOutputStream.writeInt(packet.getType());
		packetOutputStream.writeData(packet.getValue());
		
		dataOutputStream.writeInt(packetOutputStream.streamedBytes());
		dataOutputStream.write(bufferOutputStream.toByteArray());
		dataOutputStream.flush();
	}
	
}