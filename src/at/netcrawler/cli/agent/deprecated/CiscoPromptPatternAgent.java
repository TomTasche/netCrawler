package at.netcrawler.cli.agent.deprecated;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLine;
import at.netcrawler.io.deprecated.ActionOnMatchInputStream;
import at.netcrawler.io.deprecated.ActionOnMatchListener;
import at.netcrawler.io.deprecated.IgnoreLineOnMatchInputStream;


public class CiscoPromptPatternAgent extends PromptPatternAgent {
	
	private static final String SPACE = " ";
	
	public static final String PROMT_PATTERN = "(.*?)(/.*?)?(\\((.*?)\\))?(>|#)";
	public static final String COMMENT_PREFIX = "!";
	public static final Pattern MORE_PATTERN = Pattern.compile(
			".*?(.+)more\\1.*?", Pattern.CASE_INSENSITIVE);
	
	private final byte[] space;
	
	public CiscoPromptPatternAgent(CommandLine commandLine) {
		super(commandLine, PROMT_PATTERN, COMMENT_PREFIX);
		
		space = SPACE.getBytes(charset);
	}
	
	@Override
	protected InputStream commandChainHook(InputStream inputStream) {
		ActionOnMatchInputStream moreActionInputStream = new ActionOnMatchInputStream(
				inputStream, MORE_PATTERN);
		IgnoreLineOnMatchInputStream ignoreMoreInputStream = new IgnoreLineOnMatchInputStream(
				moreActionInputStream, MORE_PATTERN);
		
		moreActionInputStream.addListener(new ActionOnMatchListener() {
			public void matchOccurred(String match) {
				try {
					outputStream.write(space);
					outputStream.flush();
				} catch (IOException e) {}
			}
		});
		
		return ignoreMoreInputStream;
	}
	
}