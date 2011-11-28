package at.netcrawler.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import at.netcrawler.network.model.NetworkTopology;


public class NetSurfer {
	
	private static Server server;
	private static NetworkTopology topology;
	
	public static void startServer(NetworkTopology topology) {
		if (server == null) {
			server = new Server(1337);
			
			final ServletHandler handler = new ServletHandler();
			handler.addServletWithMapping(
					DeviceServlet.class, "/device/*");
			handler.addServletWithMapping(
					TopologyServlet.class, "/topology/*");
			server.setHandler(handler);
		}
		
		NetSurfer.topology = topology;
	}
	
	public static void stop() throws Exception {
		server.stop();
	}
	
	protected static NetworkTopology getTopology() {
		return topology;
	}
	
	public static void main(String[] args) throws Exception {
		startServer(topology);
		stop();
	}
}
