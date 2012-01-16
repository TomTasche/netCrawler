package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Set;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLineInterface;
import at.netcrawler.io.CharPrefixLineFilterReader;
import at.netcrawler.io.FilterLastLineReader;
import at.netcrawler.io.FilterLineMatchActionReader;


public class CiscoCLIAgent extends PromtPatternCLIAgent {
	
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
	
	protected final Pattern morePattern;
	protected final String moreString;
	private final Set<Character> statusPrefixes;
	
	public CiscoCLIAgent(CommandLineInterface cli,
			CiscoCLIAgentSettings settings) throws IOException {
		super(cli, settings);
		
		morePattern = settings.getMorePattern();
		moreString = settings.getMoreString();
		statusPrefixes = settings.getStatusPrefixes();
		
		handleLogin(settings);
	}
	
	public CiscoCLIAgent(CLISocket socket, CiscoCLIAgentSettings settings)
			throws IOException {
		super(socket, settings);
		
		morePattern = settings.getMorePattern();
		moreString = settings.getMoreString();
		statusPrefixes = settings.getStatusPrefixes();
		
		handleLogin(settings);
	}
	
	@Override
	protected Reader hookReader(Reader reader) {
		return new CharPrefixLineFilterReader(reader, statusPrefixes);
	}
	
	// TODO: improve
	private void handleLogin(CiscoCLIAgentSettings settings) throws IOException {
		if (settings != null) {
			if (settings.getLogonUsername() != null) out.write(settings.getLogonUsername()
					+ newLine);
			if (settings.getLogonPassword() != null) out.write(settings.getLogonPassword()
					+ newLine);
		}
	}
	
	@Override
	protected CLISocketHook getCLISocketHook(String command) {
		return new CLISocketHook() {
			public CLISocket hookSocket(CLISocket socket) {
				Reader reader = socket.getReader();
				final Writer writer = socket.getWriter();
				
				reader = new FilterLineMatchActionReader(reader, morePattern) {
					protected void match() {
						try {
							writer.write(moreString);
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
				
				return new CLISocket(reader, writer);
			}
		};
	}
	
}