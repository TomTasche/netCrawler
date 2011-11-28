package at.netcrawler.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import com.google.gson.Gson;


public class GraphWriter {
	
	private static final Charset UTF8 = Charset.forName("UTF-8");
	
	private static final Gson GSON = new Gson();
	
	public static void toJson(File file, Object graph) throws IOException {
		String json = GSON.toJson(graph);
		
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
