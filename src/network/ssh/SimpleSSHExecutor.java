package network.ssh;


public abstract class SimpleSSHExecutor {
	
	public static final int DEFAULT_PORT = 22;
	
	
	public abstract String execute(String command) throws Exception;
	
}