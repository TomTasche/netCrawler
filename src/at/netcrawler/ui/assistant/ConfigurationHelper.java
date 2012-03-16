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

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import at.netcrawler.io.json.JsonHelper;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

public class ConfigurationHelper {

	private static void validateJsonName(JsonReader reader, String name)
			throws IOException {
		if (!reader.nextName().equals(name))
			throw new IOException("Illegal JSON format!");
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

	public static void writeToJsonFile(File file, Configuration configuration) throws IOException {
		writeToJsonFile(file, Encryption.PLAIN, null, configuration);
	}

	public static void writeToJsonFile(File file, Encryption encryption,
			String password, Configuration configuration) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		JsonWriter writer = new JsonWriter(fileWriter);

		writer.beginObject();
		writer.name("encryption").value(encryption.getName());
		writer.name("data");

		if (encryption == Encryption.PLAIN) {
			writeData(writer, configuration);
		} else {
			try {
				ByteArrayOutputStream dataArrayOutputStream = new ByteArrayOutputStream();
				OutputStream dataCipherOutputStream = encryption
						.getCipherOutputStream(dataArrayOutputStream, password);
				OutputStreamWriter dataOutputStreamWriter = new OutputStreamWriter(
						dataCipherOutputStream);
				JsonWriter dataWriter = new JsonWriter(dataOutputStreamWriter);

				writeData(dataWriter, configuration);

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

	private static void writeData(JsonWriter writer, Configuration configuration) {
		JsonHelper.getGson().toJson(configuration, Configuration.class, writer);
	}
}
