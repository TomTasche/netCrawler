package at.netcrawler.test;

import java.net.Socket;

import com.mindbright.jca.security.SecureRandom;
import com.mindbright.ssh2.SSH2ConsoleRemote;
import com.mindbright.ssh2.SSH2Preferences;
import com.mindbright.ssh2.SSH2SimpleClient;
import com.mindbright.ssh2.SSH2Transport;
import com.mindbright.util.RandomSeed;
import com.mindbright.util.SecureRandomAndPad;


public class MindtermSSH2Test {
	
	public static void main(String[] args) throws Throwable {
		String address = "localhost";
		int port = 22;
		String username = "andreas";
		String password = "";
		
		SSH2Preferences prefs = new SSH2Preferences();
		
		RandomSeed seed = new RandomSeed();
		SecureRandomAndPad secureRandom = new SecureRandomAndPad(new SecureRandom(seed.getBytesBlocking(20, false)));
		
		SSH2Transport transport = new SSH2Transport(new Socket(address, port), prefs, secureRandom);
		SSH2SimpleClient client = new SSH2SimpleClient(transport, username, password);
		SSH2ConsoleRemote console = new SSH2ConsoleRemote(client.getConnection());
		client.getConnection().newSession();
		
		console.command("pwd", System.out);
		
		console.close();
		transport.normalDisconnect("quit");
		System.out.println("exit");
	}
	
}