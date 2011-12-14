package at.netcrawler.cli.agent;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLine;


public abstract class GenericCommandLineAgent<S extends CommandLineAgentSettings>
		extends CommandLineAgent {
	
	public GenericCommandLineAgent(CommandLine commandLine, S settings,
			Pattern promtPattern, String commentPrefix) {
		super(commandLine, settings, promtPattern, commentPrefix);
	}
	
	public GenericCommandLineAgent(CommandLine commandLine, S settings,
			Charset charset, Pattern promtPattern, String commentPrefix) {
		super(commandLine, settings, charset, promtPattern, commentPrefix);
	}
	
	public GenericCommandLineAgent(CommandLine commandLine, S settings,
			Charset charset, Pattern promtPattern, String commentPrefix,
			String newLine) {
		super(commandLine, settings, charset, promtPattern, commentPrefix,
				newLine);
	}
	
	public GenericCommandLineAgent(CommandLineSocket socket, S settings,
			Pattern promtPattern, String commentPrefix) {
		super(socket, settings, promtPattern, commentPrefix);
	}
	
	public GenericCommandLineAgent(CommandLineSocket socket, S settings,
			Pattern promtPattern, String commentPrefix, String newLine) {
		super(socket, settings, promtPattern, commentPrefix, newLine);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected final void initCommandLine(CommandLineAgentSettings settings)
			throws IOException {
		if (!getSettingsClass().equals(settings.getClass())) throw new IllegalArgumentException(
				"Illegal settings class!");
		
		initCommandLineGeneric((S) settings);
	}
	
	protected void initCommandLineGeneric(S settings) throws IOException {
		super.initCommandLine(settings);
	}
	
	@Override
	public abstract Class<S> getSettingsClass();
	
}