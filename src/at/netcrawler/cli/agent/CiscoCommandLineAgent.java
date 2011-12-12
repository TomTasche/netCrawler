package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLine;
import at.andiwand.library.io.StreamUtil;
import at.netcrawler.io.CharPrefixLineFilterReader;
import at.netcrawler.io.FilterLineMatchActionReader;


public class CiscoCommandLineAgent extends CommandLineAgent {
	
	private static final String COMMENT_PREFIX = "!";
	private static final Pattern PROMT_PATTERN = Pattern.compile(
			"(.*?)(/.*?)?(\\((.*?)\\))?(>|#)", Pattern.MULTILINE);
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static final Pattern MORE_PATTERN = Pattern.compile(
			".*?(.+)more\\1.*?", Pattern.CASE_INSENSITIVE);
	private static final String MORE_STRING = " ";
	private static final char[] STATUS_PREFIXES = {'%'};
	private static final String SHOW_COMMAND = "show";
	
	public CiscoCommandLineAgent(CommandLine commandLine) {
		super(commandLine, CHARSET, PROMT_PATTERN, COMMENT_PREFIX);
	}
	
	public CiscoCommandLineAgent(CommandLineSocket socket) {
		super(socket, PROMT_PATTERN, COMMENT_PREFIX);
	}
	
	@Override
	protected Reader hookReader(Reader reader) {
		return new CharPrefixLineFilterReader(reader, STATUS_PREFIXES);
	}
	
	@Override
	public String execute(String command) throws IOException {
		ProcessTerminator terminator = buildSimpleProcessTerminator();
		CommandLineSocket socket = execute(command, terminator);
		
		Reader reader = socket.getReader();
		
		if (command.toLowerCase().contains(SHOW_COMMAND)) {
			final Writer writer = socket.getWriter();
			
			reader = new FilterLineMatchActionReader(reader, MORE_PATTERN) {
				protected void match() {
					try {
						writer.write(MORE_STRING);
						writer.flush();
					} catch (IOException e) {}
				}
			};
		}
		
		return StreamUtil.read(reader);
	}
	
}