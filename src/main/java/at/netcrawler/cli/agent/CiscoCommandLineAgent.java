package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.Writer;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLineInterface;
import at.andiwand.library.io.UnlimitedPushbackInputStream;
import at.netcrawler.io.CharPrefixLineFilterInputStream;
import at.netcrawler.io.FilterLastLineInputStream;
import at.netcrawler.io.FilterLineMatchActionInputStream;


public class CiscoCommandLineAgent extends PromptCommandLineAgent {
	
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
	
	private static class MoreExecutorInputStream extends
			FilterLineMatchActionInputStream {
		private final UnlimitedPushbackInputStream in;
		private final Writer writer;
		private final String moreString;
		
		public MoreExecutorInputStream(InputStream in, Writer writer,
				Pattern morePattern, String moreString) {
			super(in, morePattern);
			
			this.in = new UnlimitedPushbackInputStream(in);
			this.writer = writer;
			this.moreString = moreString;
		}
		
		@Override
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
					case '\b':
						in.unread(read);
						flushBackspace(in);
						return;
					}
				}
			} catch (IOException e) {}
		}
	}
	
	private class MoreFilter extends ProcessTerminator {
		public MoreFilter(CommandLineInterface src) throws IOException {
			super(src);
		}
		
		@Override
		protected InputStream getFilterInputStream(InputStream in) {
			in = new MoreExecutorInputStream(in, writer, morePattern,
					moreString);
			in = new FilterLastLineInputStream(in);
			return in;
		}
	}
	
	protected final Pattern morePattern;
	protected final String moreString;
	
	public CiscoCommandLineAgent(CommandLineInterface cli,
			CiscoCommandLineAgentSettings settings) throws IOException {
		super(cli, settings);
		
		morePattern = settings.getMorePattern();
		moreString = settings.getMoreString();
		
		// handleLogin(settings);
		synchronizeStreams();
	}
	
	@Override
	protected InputStream getFilterInputStream(InputStream in,
			CommandLineAgentSettings settings) {
		return new CharPrefixLineFilterInputStream(in,
				((CiscoCommandLineAgentSettings) settings).getStatusPrefixes());
	}
	
	// TODO: improve
	// private void handleLogin(CiscoCommandLineAgentSettings settings)
	// throws IOException {
	// if (settings == null) return;
	//
	// if (settings.getLogonUsername() != null) {
	// writer.write(settings.getLogonUsername());
	// writer.write(newLine);
	// writer.flush();
	// }
	//
	// if (settings.getLogonPassword() != null) {
	// writer.write(settings.getLogonPassword());
	// writer.write(newLine);
	// writer.flush();
	// }
	// }
	
	@Override
	protected ProcessTerminator getProcessFilter(String command,
			CommandLineInterface process) throws IOException {
		return new MoreFilter(process);
	}
	
}