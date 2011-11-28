package at.netcrawler.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.prefs.Preferences;

import com.google.gson.Gson;


public class SettingsHelper {
	
	private static final Charset UTF8 = Charset.forName("UTF-8");
	
	private static final Preferences PREFERENCES = Preferences.userRoot().node(
			"netcrawler");
	private static final File SETTINGS_FILE = new File(
			PREFERENCES.absolutePath());
	
	private static final Gson GSON = new Gson();
	
	public static Settings load() throws IOException {
		FileInputStream inputStream = null;
		InputStreamReader reader = null;
		BufferedReader bufferedReader = null;
		try {
			inputStream = new FileInputStream(SETTINGS_FILE);
			reader = new InputStreamReader(inputStream, UTF8);
			bufferedReader = new BufferedReader(reader);
			
			StringBuilder builder = new StringBuilder();
			for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
					.readLine()) {
				builder.append(s);
			}
			
			return GSON.fromJson(
					builder.toString(), Settings.class);
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
	}
	
	public static void write(Settings settings) throws IOException {
		String json = GSON.toJson(settings);
		
		FileOutputStream outputStream = null;
		OutputStreamWriter writer = null;
		BufferedWriter bufferedWriter = null;
		try {
			outputStream = new FileOutputStream(SETTINGS_FILE);
			writer = new OutputStreamWriter(outputStream, UTF8);
			bufferedWriter = new BufferedWriter(writer);
			
			bufferedWriter.write(json);
			bufferedWriter.flush();
		} finally {
			bufferedWriter.close();
			writer.close();
			outputStream.close();
		}
	}
}
