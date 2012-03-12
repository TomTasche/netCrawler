package at.netcrawler.cli.agent;

import java.nio.charset.Charset;
import java.util.regex.Pattern;


public class LinuxCLIAgentSettings extends PromptCommandLineAgentSettings {
	
	public static final String DEFAULT_COMMENT_PREFIX = "#";
	public static final Pattern DEFAULT_PROMT_PATTERN = Pattern.compile(
			"(.*?)@(.*?):(.*?)\\$", Pattern.MULTILINE);
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	public LinuxCLIAgentSettings() {
		setCommentPrefix(DEFAULT_COMMENT_PREFIX);
		setPromtPattern(DEFAULT_PROMT_PATTERN);
		setCharset(DEFAULT_CHARSET);
	}
	
	public LinuxCLIAgentSettings(LinuxCLIAgentSettings settings) {
		super(settings);
	}
	
	@Override
	public LinuxCLIAgentSettings clone() {
		return new LinuxCLIAgentSettings(this);
	}
	
	@Override
	public Class<LinuxCommandLineAgent> getAgentClass() {
		return LinuxCommandLineAgent.class;
	}
	
}