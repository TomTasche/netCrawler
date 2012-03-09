package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Set;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLineInterface;
import at.netcrawler.io.CharPrefixLineFilterInputStream;
import at.netcrawler.io.FilterLastLineInputStream;
import at.netcrawler.io.FilterLineMatchActionInputStream;


public class CiscoCommandLineAgent extends PromtCommandLineAgent {
	
	private static void flushBackspace(PushbackInputStream in)
			throws IOException {
		int spaces = 0;
		
		while (true) {
			int read = in.read();
			
			switch (read) {
			case '\b':
				if (spaces > 0) spaces--;
				break;
			case ' ':
				spaces++;
				break;
			default:
				in.unread(read);
				
				if (spaces > 0) {
					for (int i = 0; i < spaces; i++) {
						in.unread(' ');
					}
				}
				
				return;
			}
		}
	}
	
	private class DefaultProcessFilter extends ProcessFilter {
		public DefaultProcessFilter(CommandLineInterface src)
				throws IOException {
			super(src);
		}
		
		@Override
		protected InputStream getFilterInputStream(InputStream in) {
			InputStream result = new FilterLineMatchActionInputStream(in,
					morePattern) {
				protected void match() {
					PushbackInputStream in = CiscoCommandLineAgent.this.in;
					
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
							case '\b':
								in.unread(read);
								flushBackspace(in);
								return;
							}
						}
					} catch (IOException e) {}
				}
			};
			result = new FilterLastLineInputStream(result);
			return result;
		}
	}
	
	protected final Pattern morePattern;
	protected final String moreString;
	private final Set<Character> statusPrefixes;
	
	public CiscoCommandLineAgent(CommandLineInterface cli,
			CiscoCommandLineAgentSettings settings) throws IOException {
		super(cli, settings);
		
		morePattern = settings.getMorePattern();
		moreString = settings.getMoreString();
		statusPrefixes = settings.getStatusPrefixes();
		
		handleLogin(settings);
	}
	
	@Override
	protected InputStream getFilterInputStream(InputStream in) {
		return new CharPrefixLineFilterInputStream(in, statusPrefixes);
	}
	
	// TODO: improve
	private void handleLogin(CiscoCommandLineAgentSettings settings)
			throws IOException {
		if (settings != null) {
			if (settings.getLogonUsername() != null) writer.write(settings
					.getLogonUsername() + newLine);
			if (settings.getLogonPassword() != null) writer.write(settings
					.getLogonPassword() + newLine);
		}
	}
	
	@Override
	protected ProcessFilter getProcessFilter(String command,
			CommandLineInterface process) throws IOException {
		return new DefaultProcessFilter(process);
	}
	
}