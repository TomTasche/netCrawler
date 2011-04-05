package at.andiwand.library.util;

import java.io.IOException;


public interface SimpleRemoteExecutor {
	
	public String execute(String command) throws IOException;
	
}