package at.netcrawler.http;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DeviceServlet extends AbstractServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		boolean parseRegex = false;
		if (request.getParameter("regex") != null) {
			parseRegex = Boolean.parseBoolean(request.getParameter("regex"));
		}
		
		// TODO: felder der geräte iterieren und matchen
		
		// TODO: only return devices containing the given fields?
		
		String[] fields = new String[0];
		if (request.getParameter("fields") != null) {
			fields = request.getParameter("fields").split(";");
		}
		
		Map<String, String[]> query = request.getParameterMap();
		query.remove("fields");
		query.remove("regex");

		// TODO: implement.
		// String json = GSON.fromJson(device, Device.class);
		// response.getWriter().println(json);
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
