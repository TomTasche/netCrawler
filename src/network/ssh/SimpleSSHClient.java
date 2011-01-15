package network.ssh;

import java.io.InputStream;
import java.io.OutputStream;


public abstract class SimpleSSHClient {
	
	public static final int DEFAULT_PORT = 22;
	
	
	public abstract InputStream getInputStream();
	public abstract OutputStream getOutputStream();
	
	public abstract void connect() throws Exception;
	public abstract void disconnect();
	
}