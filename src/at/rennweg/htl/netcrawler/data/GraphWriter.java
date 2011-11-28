package at.rennweg.htl.netcrawler.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.prefs.Preferences;

import com.google.gson.Gson;


public class GraphWriter {
	
	// TODO: don't save graph in userRoot. use save as instead
	// TODO: save preferences (like last window size) in user root
	
	private static final Charset UTF8 = Charset.forName("UTF-8");
	
	private static final Preferences PREFERENCES = Preferences.userRoot().node(
			"netcrawler");
	private static final String PREFERENCES_PATH = PREFERENCES.absolutePath();
	
	private static final Gson GSON = new Gson();
	
	public static Gson getGson() {
		return GSON;
	}
	
	public static void toJson(Object graph) throws IOException {
		String json = GSON.toJson(graph);
		
		File file = new File(PREFERENCES_PATH + System.currentTimeMillis());
		
		FileOutputStream outputStream = null;
		OutputStreamWriter writer = null;
		BufferedWriter bufferedWriter = null;
		try {
			outputStream = new FileOutputStream(file);
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
	
	public static void toHtml(File file, Object graph) {
		// TODO: implement.
		// TODO: encode images in base64
	}
}
