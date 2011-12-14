package at.netcrawler.cli.agent;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLine;


public class LinuxCommandLineAgent extends
		GenericCommandLineAgent<LinuxCommandLineAgentSettings> {
	
	private static final String COMMENT_PREFIX = "#";
	private static final Pattern PROMT_PATTERN = Pattern.compile(
			"(.*?)@(.*?):(.*?)\\$", Pattern.MULTILINE);
	private static final Charset CHARSET = Charset.forName("UTF-8");
	
	public LinuxCommandLineAgent(CommandLine commandLine,
			LinuxCommandLineAgentSettings settings) {
		super(commandLine, settings, CHARSET, PROMT_PATTERN, COMMENT_PREFIX);
	}
	
	public LinuxCommandLineAgent(CommandLineSocket socket,
			LinuxCommandLineAgentSettings settings) {
		super(socket, settings, PROMT_PATTERN, COMMENT_PREFIX);
	}
	
	// TODO: implement
	@Override
	protected void initCommandLineGeneric(LinuxCommandLineAgentSettings settings)
			throws IOException {
		super.initCommandLineGeneric(settings);
	}
	
	@Override
	public Class<LinuxCommandLineAgentSettings> getSettingsClass() {
		return LinuxCommandLineAgentSettings.class;
	}
	
}