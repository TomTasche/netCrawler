package at.netcrawler.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


@SuppressWarnings("serial")
public abstract class AbstractServlet extends HttpServlet {
	
	protected static final Gson GSON = new Gson();
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
	}
}
