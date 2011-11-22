package at.netcrawler.assistant;

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
import java.util.Map;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import at.andiwand.library.network.ip.IPv4Address;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;


public class Configuration {
	
	public static final String FILE_SUFFIX = ".conf";
	
	private static final String JSON_INDENT = "   ";
	
	private static void validateJsonName(JsonReader reader, String name)
			throws IOException {
		if (!reader.nextName().equals(name))
			throw new IOException("Illegal JSON format!");
	}
	
	
	private IPv4Address address;
	private Connection connection;
	private int port;
	private String username;
	private String password;
	private LinkedHashMap<String, String> batches = new LinkedHashMap<String, String>();
	
	
	public IPv4Address getAddress() {
		return address;
	}
	public Connection getConnection() {
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
	
	public void setAddress(IPv4Address address) {
		this.address = address;
	}
	public void setConnection(Connection connection) {
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
	
	
	public void readFromJsonFile(File file) throws IOException {
		readFromJsonFile(file, null);
	}
	
	public void readFromJsonFile(File file,
			EncryptionCallback encryptionCallback) throws IOException {
		FileReader fileReader = new FileReader(file);
		JsonReader reader = new JsonReader(fileReader);
		
		Encryption encryption;
		
		reader.beginObject();
		validateJsonName(reader, "encryption");
		encryption = Encryption.getEncryptionByName(reader.nextString());
		validateJsonName(reader, "data");
		
		if (encryption == Encryption.PLAIN) {
			readData(reader);
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
				
				readData(dataReader);
				
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
	}
	
	private void readData(JsonReader reader) throws IOException {
		reader.beginObject();
		validateJsonName(reader, "ip");
		address = IPv4Address.getByAddress(reader.nextString());
		validateJsonName(reader, "connection");
		connection = Connection.getConnectionByName(reader.nextString());
		validateJsonName(reader, "port");
		port = reader.nextInt();
		validateJsonName(reader, "username");
		username = reader.nextString();
		validateJsonName(reader, "password");
		password = reader.nextString();
		validateJsonName(reader, "batches");
		reader.beginArray();
		batches.clear();
		while (reader.hasNext()) {
			reader.beginObject();
			validateJsonName(reader, "name");
			String name = reader.nextString();
			validateJsonName(reader, "batch");
			String batch = reader.nextString();
			reader.endObject();
			batches.put(name, batch);
		}
		reader.endArray();
		reader.endObject();
	}
	
	public void writeToJsonFile(File file) throws IOException {
		writeToJsonFile(file, Encryption.PLAIN, null);
	}
	
	public void writeToJsonFile(File file, Encryption encryption,
			String password) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		JsonWriter writer = new JsonWriter(fileWriter);
		writer.setIndent(JSON_INDENT);
		
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
	
	private void writeData(JsonWriter writer) throws IOException {
		writer.beginObject();
		writer.name("ip").value(address.toString());
		writer.name("connection").value(connection.getName());
		writer.name("port").value(port);
		writer.name("username").value(username);
		writer.name("password").value(password);
		writer.name("batches");
		writer.beginArray();
		for (Map.Entry<String, String> batch : batches.entrySet()) {
			writer.beginObject();
			writer.name("name").value(batch.getKey());
			writer.name("batch").value(batch.getValue());
			writer.endObject();
		}
		writer.endArray();
		writer.endObject();
	}
	
}