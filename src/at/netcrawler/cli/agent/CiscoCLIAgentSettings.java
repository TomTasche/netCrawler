package at.netcrawler.cli.agent;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;


public class CiscoCLIAgentSettings extends PromtPatternCLIAgentSettings {
	
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	public static final String DEFAULT_COMMENT_PREFIX = "!";
	public static final String DEFAULT_NEW_LINE = "\r";
	public static final Pattern DEFAULT_PROMT_PATTERN = Pattern.compile(
			"(.*?)(/.*?)?(\\((.*?)\\))?(>|#)", Pattern.MULTILINE);
	public static final Pattern DEFAULT_MORE_PATTERN = Pattern.compile(
			"(.+).*?(.+)more\\2.*?\\1", Pattern.CASE_INSENSITIVE);
	public static final String DEFAULT_MORE_STRING = " ";
	public static final Set<Character> DEFAULT_STATUS_PREFIXES = Collections.unmodifiableSet(new HashSet<Character>(
			Arrays.asList(new Character[] {'%', '*'})));
	
	private Pattern morePattern;
	private String moreString;
	
	private Set<Character> statusPrefixes;
	
	private String logonUsername;
	private String logonPassword;
	
	private String enablePassword;
	
	public CiscoCLIAgentSettings() {
		setCharset(DEFAULT_CHARSET);
		setCommentPrefix(DEFAULT_COMMENT_PREFIX);
		setNewLine(DEFAULT_NEW_LINE);
		setPromtPattern(DEFAULT_PROMT_PATTERN);
		setMorePattern(DEFAULT_MORE_PATTERN);
		setMoreString(DEFAULT_MORE_STRING);
		setStatusPrefixes(statusPrefixes);
	}
	
	public CiscoCLIAgentSettings(CiscoCLIAgentSettings settings) {
		super(settings);
		
		logonUsername = settings.logonUsername;
		logonPassword = settings.logonPassword;
		enablePassword = settings.enablePassword;
	}
	
	@Override
	public CiscoCLIAgentSettings clone() {
		return new CiscoCLIAgentSettings(this);
	}
	
	@Override
	public Class<CiscoCLIAgent> getAgentClass() {
		return CiscoCLIAgent.class;
	}
	
	public String getLogonUsername() {
		return logonUsername;
	}
	
	public String getLogonPassword() {
		return logonPassword;
	}
	
	public String getEnablePassword() {
		return enablePassword;
	}
	
	public Pattern getMorePattern() {
		return morePattern;
	}
	
	public String getMoreString() {
		return moreString;
	}
	
	public Set<Character> getStatusPrefixes() {
		return statusPrefixes;
	}
	
	public void setLogonUsername(String logonUsername) {
		this.logonUsername = logonUsername;
	}
	
	public void setLogonPassword(String logonPassword) {
		this.logonPassword = logonPassword;
	}
	
	public void setEnablePassword(String enablePassword) {
		this.enablePassword = enablePassword;
	}
	
	public void setMorePattern(Pattern morePattern) {
		this.morePattern = morePattern;
	}
	
	public void setMoreString(String moreString) {
		this.moreString = moreString;
	}
	
	public void setStatusPrefixes(Set<Character> statusPrefixes) {
		this.statusPrefixes = Collections.unmodifiableSet(new HashSet<Character>(
				statusPrefixes));
	}
	
}