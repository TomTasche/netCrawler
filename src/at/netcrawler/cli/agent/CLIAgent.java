package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CLIExecutor;
import at.andiwand.library.cli.CommandLineInterface;
import at.andiwand.library.io.FluidInputStreamReader;
import at.andiwand.library.io.StreamUtil;
import at.andiwand.library.io.UnlimitedPushbackReader;


public abstract class CLIAgent implements CLIExecutor {
	
	protected final UnlimitedPushbackReader in;
	protected final Writer out;
	
	private CLIProcessTerminator lastTerminator;
	
	public CLIAgent(CommandLineInterface cli, CLIAgentSettings settings)
			throws IOException {
		in = initReader(cli.getInputStream(), settings.getCharset());
		out = initWriter(cli.getOutputStream(), settings.getCharset());
	}
	
	public CLIAgent(CLISocket socket, CLIAgentSettings settings) {
		in = initReader(socket.getReader());
		out = initWriter(socket.getWriter());
	}
	
	private UnlimitedPushbackReader initReader(InputStream in, Charset charset) {
		in = hookInputStream(in);
		
		Reader reader = new FluidInputStreamReader(in, charset);
		return initReader(reader);
	}
	
	private UnlimitedPushbackReader initReader(Reader reader) {
		reader = hookReader(reader);
		return new UnlimitedPushbackReader(reader);
	}
	
	private Writer initWriter(OutputStream out, Charset charset) {
		out = hookOutputStream(out);
		
		Writer writer = new OutputStreamWriter(out, charset);
		return initWriter(writer);
	}
	
	private Writer initWriter(Writer writer) {
		writer = hookWriter(writer);
		return writer;
	}
	
	protected InputStream hookInputStream(InputStream in) {
		return in;
	}
	
	protected OutputStream hookOutputStream(OutputStream out) {
		return out;
	}
	
	protected Reader hookReader(Reader reader) {
		return reader;
	}
	
	protected Writer hookWriter(Writer writer) {
		return writer;
	}
	
	protected abstract CLISocketHook getCLISocketHook(String command);
	
	protected abstract CLIProcessTerminator getCLIProcessTerminator(
			String command);
	
	protected final Matcher match(Pattern pattern) throws IOException {
		return StreamUtil.match(in, pattern);
	}
	
	protected final Matcher find(Pattern pattern) throws IOException {
		return StreamUtil.find(in, pattern);
	}
	
	protected final String readLine() throws IOException {
		return StreamUtil.readLine(in);
	}
	
	protected final void flushLine() throws IOException {
		StreamUtil.flushLine(in);
	}
	
	protected abstract void rawExecute(String command) throws IOException;
	
	public final CLISocket execute(String command, CLISocketHook socketHook,
			CLIProcessTerminator terminator) throws IOException {
		if (lastTerminator != null) {
			try {
				lastTerminator.waitFor();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		
		rawExecute(command);
		
		CLISocket socket = new CLISocket(in, out);
		socket = terminator.hookSocket(socket);
		socket = socketHook.hookSocket(socket);
		
		lastTerminator = terminator;
		
		return socket;
	}
	
	public final String execute(String command, CLISocketHook socketHook)
			throws IOException {
		CLIProcessTerminator terminator = getCLIProcessTerminator(command);
		CLISocket socket = execute(command, socketHook, terminator);
		
		return StreamUtil.read(socket.getReader());
	}
	
	public final String execute(String command) throws IOException {
		CLISocketHook socketHook = getCLISocketHook(command);
		
		return execute(command, socketHook);
	}
	
}