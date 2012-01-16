package at.netcrawler.cli.agent;

import java.util.regex.Pattern;


public abstract class PromtPatternCLIAgentSettings extends CLIAgentSettings {
	
	public static final String DEFAULT_NEW_LINE = "\n";
	
	private Pattern promtPattern;
	private String commentPrefix;
	private String newLine;
	
	public PromtPatternCLIAgentSettings() {
		setNewLine(DEFAULT_NEW_LINE);
	}
	
	public PromtPatternCLIAgentSettings(PromtPatternCLIAgentSettings settings) {
		super(settings);
		
		promtPattern = settings.promtPattern;
		commentPrefix = settings.commentPrefix;
		newLine = settings.newLine;
	}
	
	@Override
	public abstract PromtPatternCLIAgentSettings clone();
	
	@Override
	public abstract Class<? extends PromtPatternCLIAgent> getAgentClass();
	
	public Pattern getPromtPattern() {
		return promtPattern;
	}
	
	public String getCommentPrefix() {
		return commentPrefix;
	}
	
	public String getNewLine() {
		return newLine;
	}
	
	public void setPromtPattern(Pattern promtPattern) {
		this.promtPattern = promtPattern;
	}
	
	public void setCommentPrefix(String commentPrefix) {
		this.commentPrefix = commentPrefix;
	}
	
	public void setNewLine(String newLine) {
		this.newLine = newLine;
	}
	
}