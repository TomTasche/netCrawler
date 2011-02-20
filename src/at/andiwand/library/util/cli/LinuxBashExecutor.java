package at.andiwand.library.util.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import at.andiwand.library.util.stream.IgnoreFirstLineInputStream;
import at.andiwand.library.util.stream.IgnoreLastLineInputStream;
import at.andiwand.library.util.stream.IgnoreLineByMatchInputStream;
import at.andiwand.library.util.stream.ReadAfterMatchInputStream;
import at.andiwand.library.util.stream.ReadUntilMatchInputStream;



// TODO: create pattern
public class LinuxBashExecutor extends CommandLineExecutor {
	
	public static final Pattern PROMT_PATTERN = Pattern.compile("andreas@andreas-desktop");
	
	
	
	public LinuxBashExecutor(CommandLine commandLine) throws IOException {
		super(commandLine);
		
		InputStream inputStream = commandLine.getInputStream();
		InputStream killerStream = new ReadUntilMatchInputStream(inputStream, PROMT_PATTERN);
		
		try {
			while (killerStream.read() != -1);
		} catch (IOException e) {}
	}
	
	
	
	@Override
	public Command execute(String command) throws IOException {
		Pattern commandPattern = Pattern.compile(".*" + command);
		
		InputStream inputStream = commandLine.getInputStream();
		InputStream stream1 = new ReadAfterMatchInputStream(inputStream, commandPattern);
		InputStream stream2 = new IgnoreFirstLineInputStream(stream1);
		InputStream stream3 = new ReadUntilMatchInputStream(stream2, PROMT_PATTERN);
		InputStream stream4 = new IgnoreLastLineInputStream(stream3);
		InputStream processInputStream = new IgnoreLineByMatchInputStream(stream4, Pattern.compile("USER.*", Pattern.DOTALL));
		
		OutputStream outputStream = commandLine.getOutputStream();
		outputStream.write((command + "\n").getBytes());
		outputStream.flush();
		
		return new LinuxCommand(processInputStream, commandLine.getOutputStream());
	}
	
	
	private static class LinuxCommand extends Command {
		private InputStream inputStream;
		private OutputStream outputStream;
		
		public LinuxCommand(InputStream inputStream, OutputStream outputStream) {
			this.inputStream = inputStream;
			this.outputStream = outputStream;
		}
		
		public InputStream getErrorStream() {return null;}
		public InputStream getInputStream() {
			return inputStream;
		}
		public OutputStream getOutputStream() {
			return outputStream;
		}
		
		public int waitFor() throws InterruptedException {return 0;}
		
		public int exitValue() {return 0;}
		public void destroy() {}
	}
	
}