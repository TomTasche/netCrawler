package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLine;
import at.netcrawler.io.CharPrefixLineFilterReader;
import at.netcrawler.io.FilterLastLineReader;
import at.netcrawler.io.FilterLineMatchActionReader;


public class CiscoCommandLineAgent extends CommandLineAgent {
	
	private static final String NEW_LINE = "\r";
	private static final String COMMENT_PREFIX = "!";
	private static final Pattern PROMT_PATTERN = Pattern.compile(
			"(.*?)(/.*?)?(\\((.*?)\\))?(>|#)", Pattern.MULTILINE);
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static final Pattern MORE_PATTERN = Pattern.compile(
			"(.+).*?(.+)more\\2.*?\\1", Pattern.CASE_INSENSITIVE);
	private static final String MORE_STRING = " ";
	private static final char[] STATUS_PREFIXES = {'%'};
	
	public CiscoCommandLineAgent(CommandLine commandLine) {
		super(commandLine, CHARSET, PROMT_PATTERN, COMMENT_PREFIX, NEW_LINE);
	}
	
	public CiscoCommandLineAgent(CommandLineSocket socket) {
		super(socket, PROMT_PATTERN, COMMENT_PREFIX, NEW_LINE);
	}
	
	@Override
	protected Reader hookReader(Reader reader) {
		return new CharPrefixLineFilterReader(reader, STATUS_PREFIXES);
	}
	
	@Override
	protected CommandLineSocketHook buildSimpleCommandLineSocketHook() {
		return new CommandLineSocketHook() {
			public CommandLineSocket hookSocket(CommandLineSocket socket) {
				Reader reader = socket.getReader();
				final Writer writer = socket.getWriter();
				
				reader = new FilterLineMatchActionReader(reader, MORE_PATTERN) {
					protected void match() {
						try {
							writer.write(MORE_STRING);
							writer.flush();
							
							while (true) {
								int read = in.read();
								if (read == -1) return;
								if (read == 0x12) return;
							}
						} catch (IOException e) {}
					}
				};
				
				reader = new FilterLastLineReader(reader);
				
				return new CommandLineSocket(reader, writer);
			}
		};
	}
	
}