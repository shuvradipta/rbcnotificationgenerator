package com.rbc._3m00.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddEditServiceNoticeServlet
 */
@WebServlet("/UpdateServiceNoticeServlet")
public class AddEditServiceNoticeServlet extends HttpServlet {
	private static final String ADD_EDIT_SERVICE_NOTICE_JSP = "addEditServiceNotice.jsp";
	
	private static String[] appEnvMappings = {"HTMLGENAPPURL" , "SERVICENOTICEGENAPPURL", "MOBILEGENAPPURL"};
	
	private static HashMap<String,String> appConfigMappings =  null;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddEditServiceNoticeServlet() {
        super();
    }
    

	@Override
	public void init() throws ServletException {
		appConfigMappings = initializeAppConfigMappings();
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		populateRequestAttributes(request);
		
		request.getRequestDispatcher(ADD_EDIT_SERVICE_NOTICE_JSP).forward(request, response);
	}


	private void populateRequestAttributes(HttpServletRequest request) {
		request.setAttribute("startTime", request.getParameter("startTime"));
		request.setAttribute("expiryTime", request.getParameter("expiryTime"));
		request.setAttribute("url", request.getParameter("url"));
		request.setAttribute("kioskInd", request.getParameter("kioskInd"));
		request.setAttribute("publicInd", request.getParameter("publicInd"));
		request.setAttribute("noticeText", request.getParameter("noticeText"));
		request.setAttribute("appConfigMappings", appConfigMappings);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private HashMap<String,String> initializeAppConfigMappings(){
		HashMap<String,String> appUrlMappings = new HashMap<String, String>();
		for (String envVar : appEnvMappings) {
			appUrlMappings.put(envVar, System.getenv(envVar));
		}
		//System.out.println("ServiceNoticeServlet.initializeAppConfigMappings() :: " + appUrlMappings);
		return appUrlMappings;
		
	}

}
