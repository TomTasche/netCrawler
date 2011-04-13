package at.rennweg.htl.netcrawler.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import at.andiwand.library.util.cli.CommandLine;
import at.rennweg.htl.netcrawler.util.stream.ActionOnMatchInputStream;
import at.rennweg.htl.netcrawler.util.stream.IgnoreFirstLineInputStream;
import at.rennweg.htl.netcrawler.util.stream.IgnoreLastLineInputStream;
import at.rennweg.htl.netcrawler.util.stream.MatchActionListener;
import at.rennweg.htl.netcrawler.util.stream.ReadUntilMatchInputStream;


public class SimpleCiscoCommandLineExecutor extends SimpleCommandLineExecutor {
	
	public static final Pattern PROMT_PATTERN = Pattern.compile("(.*?)(/.*?)?(\\((.*?)\\))?(>|#)");
	public static final Pattern MORE_PATTERN = Pattern.compile("( *)(-+)( *)more\\3\\2\\1", Pattern.CASE_INSENSITIVE);
	
	public static final byte[] NEW_LINE = {'\r', '\n'};
	public static final byte[] NEXT_PAGE = {' '};
	
	
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public SimpleCiscoCommandLineExecutor(CommandLine commandLine) throws IOException {
		super(commandLine);
		
		inputStream = commandLine.getInputStream();
		outputStream = commandLine.getOutputStream();
	}
	
	@Override
	public String execute(String command) throws IOException {
		for (int i = 0; i < command.length(); i++) {
			char c = command.charAt(i);
			
			outputStream.write(c);
			outputStream.flush();
			
			if (Character.isWhitespace(c)) continue;
			
			Pattern charPattern = Pattern.compile(".*" + c);
			InputStream killerStream = new ReadUntilMatchInputStream(inputStream, charPattern);
			while (killerStream.read() != -1) ;
		}
		
		outputStream.write(NEW_LINE);
		outputStream.flush();
		
		
		IgnoreFirstLineInputStream ignoreFirst = new IgnoreFirstLineInputStream(inputStream);
		ActionOnMatchInputStream moreAction = new ActionOnMatchInputStream(ignoreFirst, MORE_PATTERN);
		ReadUntilMatchInputStream readUntil = new ReadUntilMatchInputStream(moreAction, PROMT_PATTERN);
		IgnoreLastLineInputStream ignoreLast = new IgnoreLastLineInputStream(readUntil);
		
		moreAction.addListener(new MatchActionListener() {
			public void matchOccurred(String match) {
				try {
					byte[] tmp = new byte[inputStream.available()];
					inputStream.read(tmp);
					
					outputStream.write(NEXT_PAGE);
					outputStream.flush();
				} catch (Exception e) {}
			}
		});
		
		StringBuilder builder = new StringBuilder();
		
		int read;
		while ((read = ignoreLast.read()) != -1) {
			builder.append((char) read);
		}
		
		return builder.toString();
	}
	
}