package at.netcrawler.test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Test {
	
	public static void main(String[] args) throws Throwable {
		String host = "192.168.15.101";
		int port = 23;
		
		Socket socket = new Socket(host, port);
		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
		
		out.write("cisco\r".getBytes());
		out.flush();
		Thread.sleep(500);
		out.write("cisco\r".getBytes());
		out.flush();
		Thread.sleep(500);
		out.write("show version\r".getBytes());
		out.flush();
		
		int read;
		while ((read = in.read()) != -1) {
			System.out.write(read);
			System.out.flush();
		}
	}
	
}