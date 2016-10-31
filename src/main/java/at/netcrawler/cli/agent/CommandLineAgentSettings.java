package at.netcrawler.cli.agent;

import java.nio.charset.Charset;


public abstract class CommandLineAgentSettings implements Cloneable {
	
	public static final Charset DEFAULT_CHARSET = Charset.forName("US-ASCII");
	
	private Charset charset;
	
	public CommandLineAgentSettings() {
		setCharset(DEFAULT_CHARSET);
	}
	
	public CommandLineAgentSettings(CommandLineAgentSettings settings) {
		charset = settings.charset;
	}
	
	@Override
	public abstract CommandLineAgentSettings clone();
	
	public abstract Class<? extends CommandLineAgent> getAgentClass();
	
	public Charset getCharset() {
		return charset;
	}
	
	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	
}