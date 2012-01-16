package at.netcrawler.cli.agent;

import java.nio.charset.Charset;
import java.util.regex.Pattern;


public class LinuxCLIAgentSettings extends PromtPatternCLIAgentSettings {
	
	public static final String DEFAULT_COMMENT_PREFIX = "#";
	public static final Pattern DEFAULT_PROMT_PATTERN = Pattern.compile(
			"(.*?)@(.*?):(.*?)\\$", Pattern.MULTILINE);
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	private String userPassword;
	
	public LinuxCLIAgentSettings() {
		setCommentPrefix(DEFAULT_COMMENT_PREFIX);
		setPromtPattern(DEFAULT_PROMT_PATTERN);
		setCharset(DEFAULT_CHARSET);
	}
	
	public LinuxCLIAgentSettings(LinuxCLIAgentSettings settings) {
		super(settings);
		
		userPassword = settings.userPassword;
	}
	
	@Override
	public LinuxCLIAgentSettings clone() {
		return new LinuxCLIAgentSettings(this);
	}
	
	@Override
	public Class<LinuxCLIAgent> getAgentClass() {
		return LinuxCLIAgent.class;
	}
	
	public String getUserPassword() {
		return userPassword;
	}
	
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
}