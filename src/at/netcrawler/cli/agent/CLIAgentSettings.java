package at.netcrawler.cli.agent;

import java.nio.charset.Charset;


public abstract class CLIAgentSettings implements Cloneable {
	
	public static final Charset DEFAULT_CHARSET = Charset.forName("US-ASCII");
	
	private Charset charset;
	
	public CLIAgentSettings() {
		setCharset(DEFAULT_CHARSET);
	}
	
	public CLIAgentSettings(CLIAgentSettings settings) {
		charset = settings.charset;
	}
	
	@Override
	public abstract CLIAgentSettings clone();
	
	public abstract Class<? extends PromtPatternCLIAgent> getAgentClass();
	
	public Charset getCharset() {
		return charset;
	}
	
	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	
}