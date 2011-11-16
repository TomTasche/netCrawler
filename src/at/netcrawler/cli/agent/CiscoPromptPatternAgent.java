package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLine;
import at.netcrawler.stream.ActionOnMatchInputStream;
import at.netcrawler.stream.ActionOnMatchListener;
import at.netcrawler.stream.IgnoreLineOnMatchInputStream;


public class CiscoPromptPatternAgent extends PromptPatternAgent {
	
	public static final Pattern PROMT_PATTERN = Pattern
			.compile("(.*?)(/.*?)?(\\((.*?)\\))?(>|#)");
	public static final String COMMENT_PREFIX = "!";
	public static final Pattern MORE_PATTERN = Pattern.compile(
			" *-+ ?more ?-+ *", Pattern.CASE_INSENSITIVE);
	
	public CiscoPromptPatternAgent(CommandLine commandLine) {
		super(commandLine, PROMT_PATTERN, COMMENT_PREFIX);
	}
	
	@Override
	protected InputStream commandChainHook(InputStream inputStream) {
		ActionOnMatchInputStream moreAction = new ActionOnMatchInputStream(
				inputStream, MORE_PATTERN);
		IgnoreLineOnMatchInputStream killMore = new IgnoreLineOnMatchInputStream(
				moreAction, MORE_PATTERN);
		
		moreAction.addListener(new ActionOnMatchListener() {
			public void matchOccurred(String match) {
				try {
					OutputStream outputStream = commandLine.getOutputStream();
					outputStream.write(" ".getBytes());
					outputStream.flush();
				} catch (IOException e) {}
			}
		});
		
		return killMore;
	}
	
}