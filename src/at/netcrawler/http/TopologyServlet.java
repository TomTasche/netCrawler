package at.netcrawler.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class TopologyServlet extends AbstractServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		// TODO: implement.
		// String json = GSON.fromJson(topology, Topology.class);
		// response.getWriter().println(json);
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
