package at.netcrawler.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class NetSurfer {

	private static final Server server;
	
	static {
		server = new Server(1337);
		
		final ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(DeviceServlet.class, "/device/*");
		handler.addServletWithMapping(TopologyServlet.class, "/topology/*");
		server.setHandler(handler);
	}
	
	
	public static void start() throws Exception {
		server.start();
	}
	
	public static void stop() throws Exception {
		server.stop();
	}
	
	
	public static void main(String[] args) throws Exception {
		start();
		stop();
	}
}
