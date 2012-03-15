package at.netcrawler.ui.assistant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.Set;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.io.json.JsonHelper;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;


// TODO: accessor, connection class, connection settings
public class Configuration {
	
	public static final String FILE_SUFFIX = ".conf";
	
	private static void validateJsonName(JsonReader reader, String name)
			throws IOException {
		if (!reader.nextName().equals(name))
			throw new IOException("Illegal JSON format!");
	}
	
	private Set<IPv4Address> addresses;
	private ConnectionContainer connection;
	private int port;
	private String username;
	private String password;
	private LinkedHashMap<String, String> batches = new LinkedHashMap<String, String>();
	
	public Set<IPv4Address> getAddresses() {
		return addresses;
	}
	
	public ConnectionContainer getConnectionContainer() {
		return connection;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public LinkedHashMap<String, String> getBatches() {
		return new LinkedHashMap<String, String>(batches);
	}
	
	public String getBatch(String name) {
		return batches.get(name);
	}
	
	public void setAddresses(Set<IPv4Address> addresses) {
		this.addresses = addresses;
	}
	
	public void setConnection(ConnectionContainer connection) {
		this.connection = connection;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setBatches(LinkedHashMap<String, String> batches) {
		this.batches = new LinkedHashMap<String, String>(batches);
	}
	
	public void putBatch(String name, String batch) {
		batches.put(name, batch);
	}
	
	public void removeBatch(String name) {
		batches.remove(name);
	}
	
	public static Configuration readFromJsonFile(File file) throws IOException {
		return readFromJsonFile(file, null);
	}
	
	public static Configuration readFromJsonFile(File file,
			EncryptionCallback encryptionCallback) throws IOException {
		FileReader fileReader = new FileReader(file);
		JsonReader reader = new JsonReader(fileReader);
		
		Configuration configuration;
		Encryption encryption;
		
		reader.beginObject();
		validateJsonName(reader, "encryption");
		encryption = Encryption.getEncryptionByName(reader.nextString());
		validateJsonName(reader, "data");
		
		if (encryption == Encryption.PLAIN) {
			configuration = readData(reader);
		} else {
			try {
				String password = encryptionCallback.getPassword(encryption);
				
				String data = reader.nextString();
				byte[] dataArray = new BASE64Decoder().decodeBuffer(data);
				ByteArrayInputStream dataArrayInputStream = new ByteArrayInputStream(
						dataArray);
				InputStream dataCipherInputStream = encryption
						.getCipherInputStream(dataArrayInputStream, password);
				InputStreamReader dataInputStreamReader = new InputStreamReader(
						dataCipherInputStream);
				JsonReader dataReader = new JsonReader(dataInputStreamReader);
				
				configuration = readData(dataReader);
				
				dataReader.close();
				dataInputStreamReader.close();
				dataCipherInputStream.close();
				dataArrayInputStream.close();
			} catch (MalformedJsonException e) {
				throw new IOException("Wrong password!");
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
		
		reader.endObject();
		
		reader.close();
		fileReader.close();
		
		return configuration;
	}
	
	private static Configuration readData(JsonReader reader) throws IOException {
		return JsonHelper.getGson().fromJson(reader, Configuration.class);
	}
	
	public void writeToJsonFile(File file) throws IOException {
		writeToJsonFile(file, Encryption.PLAIN, null);
	}
	
	public void writeToJsonFile(File file, Encryption encryption,
			String password) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		JsonWriter writer = new JsonWriter(fileWriter);
		
		writer.beginObject();
		writer.name("encryption").value(encryption.getName());
		writer.name("data");
		
		if (encryption == Encryption.PLAIN) {
			writeData(writer);
		} else {
			try {
				ByteArrayOutputStream dataArrayOutputStream = new ByteArrayOutputStream();
				OutputStream dataCipherOutputStream = encryption
						.getCipherOutputStream(dataArrayOutputStream, password);
				OutputStreamWriter dataOutputStreamWriter = new OutputStreamWriter(
						dataCipherOutputStream);
				JsonWriter dataWriter = new JsonWriter(dataOutputStreamWriter);
				
				writeData(dataWriter);
				
				dataWriter.close();
				dataOutputStreamWriter.close();
				dataCipherOutputStream.close();
				dataArrayOutputStream.close();
				
				String data = new BASE64Encoder().encode(dataArrayOutputStream
						.toByteArray());
				writer.value(data);
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
		
		writer.endObject();
		
		writer.close();
		fileWriter.close();
	}

	private void writeData(JsonWriter writer) {
		JsonHelper.getGson().toJson(this, Configuration.class, writer);
	}
}