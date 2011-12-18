package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLine;
import at.netcrawler.io.CharPrefixLineFilterReader;
import at.netcrawler.io.FilterLastLineReader;
import at.netcrawler.io.FilterLineMatchActionReader;


public class CiscoCommandLineAgent extends
		GenericCommandLineAgent<CiscoCommandLineAgentSettings> {
	
	private static final String NEW_LINE = "\r";
	private static final String COMMENT_PREFIX = "!";
	private static final Pattern PROMT_PATTERN = Pattern.compile(
			"(.*?)(/.*?)?(\\((.*?)\\))?(>|#)", Pattern.MULTILINE);
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static final Pattern MORE_PATTERN = Pattern.compile(
			"(.+).*?(.+)more\\2.*?\\1", Pattern.CASE_INSENSITIVE);
	private static final String MORE_STRING = " ";
	private static final char[] STATUS_PREFIXES = {'%', '*'};
	
	public CiscoCommandLineAgent(CommandLine commandLine,
			CiscoCommandLineAgentSettings settings) {
		super(commandLine, settings, CHARSET, PROMT_PATTERN, COMMENT_PREFIX,
				NEW_LINE);
	}
	
	public CiscoCommandLineAgent(CommandLineSocket socket,
			CiscoCommandLineAgentSettings settings) {
		super(socket, settings, PROMT_PATTERN, COMMENT_PREFIX, NEW_LINE);
	}
	
	@Override
	protected Reader hookReader(Reader reader) {
		return new CharPrefixLineFilterReader(reader, STATUS_PREFIXES);
	}
	
	// TODO: improve
	@Override
	protected void initCommandLineGeneric(CiscoCommandLineAgentSettings settings)
			throws IOException {
		if (settings != null) {
			if (settings.getLogonUsername() != null) out.write(settings.getLogonUsername()
					+ NEW_LINE);
			if (settings.getLogonPassword() != null) out.write(settings.getLogonPassword()
					+ NEW_LINE);
		}
		
		super.initCommandLineGeneric(settings);
	}
	
	@Override
	public Class<CiscoCommandLineAgentSettings> getSettingsClass() {
		return CiscoCommandLineAgentSettings.class;
	}
	
	private static void flushBackspace(PushbackReader in) throws IOException {
		int spaces = 0;
		
		while (true) {
			int read = in.read();
			
			switch (read) {
			case 8:
				if (spaces > 0) spaces--;
				break;
			case ' ':
				spaces++;
				break;
			
			default:
				in.unread(read);
				
				if (spaces > 0) {
					for (int i = 0; i < spaces; i++)
						in.unread(' ');
				}
				
				return;
			}
		}
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
								
								switch (read) {
								case -1:
								case 18:
								case '\n':
								case '\r':
									return;
								case 8:
									in.unread(read);
									flushBackspace(in);
									return;
								}
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