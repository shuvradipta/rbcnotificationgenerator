package com.rbc._3m00.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;

import com.rbc._3m00.dto.Notice;
import com.rbc._3m00.util.NoticeMappingInitializer;
import com.rbc._3m00.util.ServiceNoticeJSGenerator;

/**
 * Servlet implementation class ServiceNoticeServlet
 */
@WebServlet("/ServiceNoticeServlet")
public class ServiceNoticeServlet extends HttpServlet {
	private static final String TIMEZONE_AMERICA_TORONTO = "America/Toronto";

	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";

	private static final String RESOURCES_NOTICE_URL_MAPPING_EN_TXT = "/resources/notice_url_mapping_en.txt";
	
	private static final String RESOURCES_NOTICE_URL_MAPPING_FR_TXT = "/resources/notice_url_mapping_fr.txt";

	private static final String SERVICE_NOTICE_JSP = "serviceNotice.jsp";

	private static final String SERVICENOTICE_JS_EN_PATH = "/js/servicenotice_en.js";
	
	private static final String SERVICENOTICE_JS_FR_PATH = "/js/servicenotice_fr.js";
	
	private static final String SERVICE_NOTICE_JS_INIT_FILE = "/js/servicenotice_init.js";

	private static final String PROCESSING_STATUS = "PROCESSING_STATUS";

	private static final long serialVersionUID = 1L;
	
	private static final String[] tableColumnNames = new String[]{"Notice Text", /*"URL",*/"Public","Start Time","Expiry Time","Kiosk","Add"};
	
	private static ArrayList<Notice> enNoticeMappings = null;
	
	private static ArrayList<Notice> frNoticeMappings = null;
	
	private static String[] appEnvMappings = {"HTMLGENAPPURL" , "SERVICENOTICEGENAPPURL", "MOBILEGENAPPURL"};
	
	private static HashMap<String,String> appConfigMappings =  null;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServiceNoticeServlet() {
        super();
    }

	@Override
	public void init() throws ServletException {
		super.init();
		enNoticeMappings = NoticeMappingInitializer.getInstance().initializeNoticeMapping(this.getServletContext().getRealPath(RESOURCES_NOTICE_URL_MAPPING_EN_TXT));
		frNoticeMappings = NoticeMappingInitializer.getInstance().initializeNoticeMapping(this.getServletContext().getRealPath(RESOURCES_NOTICE_URL_MAPPING_FR_TXT));
		appConfigMappings = initializeAppConfigMappings();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Initialize English and French frequent notices
		request.setAttribute("enNoticeMappings", enNoticeMappings);
		request.setAttribute("frNoticeMappings", frNoticeMappings);
		request.setAttribute("serverDateTime", getServerDateTime());
		request.setAttribute("tableColumnNames", tableColumnNames);
		request.setAttribute("appConfigMappings", appConfigMappings);
		
		request.getRequestDispatcher(SERVICE_NOTICE_JSP).forward(request, response);
	}

	/**
	 * @throws IOException 
	 * @throws ServletException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		boolean processingSuccessfulEn = writeToFile(ServiceNoticeJSGenerator.getInstance().generateCode(extractNoticesFromRequest(request, enNoticeMappings), this.getServletContext().getRealPath(SERVICE_NOTICE_JS_INIT_FILE)), SERVICENOTICE_JS_EN_PATH);
		boolean processingSuccessfulFr = writeToFile(ServiceNoticeJSGenerator.getInstance().generateCode(extractNoticesFromRequest(request, frNoticeMappings), this.getServletContext().getRealPath(SERVICE_NOTICE_JS_INIT_FILE)), SERVICENOTICE_JS_FR_PATH);
		
		request.setAttribute(PROCESSING_STATUS, processingSuccessfulEn && processingSuccessfulFr);
		/*request.setAttribute("tableColumnNames", tableColumnNames);
		//Initialize English and French frequent notices
		request.setAttribute("enNoticeMappings", enNoticeMappings);
		request.setAttribute("frNoticeMappings", frNoticeMappings);
		request.setAttribute("serverDateTime", getServerDateTime());
		request.setAttribute("appConfigMappings", appConfigMappings);
		
		request.getRequestDispatcher(SERVICE_NOTICE_JSP).forward(request, response);*/
		
		doGet(request, response);
	}
				

	private ArrayList<Notice> extractNoticesFromRequest(HttpServletRequest request, ArrayList<Notice> noticeMappings){
		ArrayList<Notice> notices = new ArrayList<Notice>();
		String noticeKeyList = formatJSArray(request.getParameter("noticeKeyList").trim());
		System.out.println("ServiceNoticeServlet.extractNoticesFromRequest() - noticeKeyList ::" + noticeKeyList);
		String[] noticeKeys = noticeKeyList.split(",");
		for (int i = 0; i < noticeKeys.length; i++) {
			for (Notice notice : noticeMappings) {
				if(notice.getKey().equalsIgnoreCase(noticeKeys[i])){
					notice.setStartTime(formatDateTime(request.getParameter("startTime"+notice.getKey())));
					notice.setExpiryTime(formatDateTime(request.getParameter("expiryTime"+notice.getKey())));
					notices.add(notice);
					break;
				}
			}
		}
		System.out.println("ServiceNoticeServlet.extractNoticesFromRequest():\n" + notices);
		return notices;
	}
	
	private String formatJSArray(String noticeKeyList) {
		noticeKeyList = noticeKeyList.replaceAll(Pattern.quote("["), "");
		noticeKeyList = noticeKeyList.replaceAll(Pattern.quote("]"), "");
		noticeKeyList = noticeKeyList.replaceAll(Pattern.quote("\""), "");
		
		return noticeKeyList;
	}
	
	private boolean writeToFile(String jsCode, String filePath){
		boolean processingSuccessful = false;
		try{
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(new File(this.getServletContext().getRealPath(filePath))));
			fileWriter.append(jsCode);
			fileWriter.flush();
			fileWriter.close();
			processingSuccessful = true;
		}catch (IOException e) {
			e.printStackTrace();
			processingSuccessful = false;
		}
		return processingSuccessful;
	}

	private String formatDateTime(String dateTime) {
		if(null != dateTime && !"null".equalsIgnoreCase(dateTime) && !"".equalsIgnoreCase(dateTime.trim())){
			dateTime = dateTime.replaceAll("-", "");
			dateTime = dateTime.replaceAll("T", "");
			dateTime = dateTime.replaceAll(":", "");
			dateTime = dateTime.concat("00");
		}
		System.out.println("formatDateTime = " + dateTime);
		return dateTime;
	}
	
	private String getServerDateTime(){
		TimeZone.setDefault(TimeZone.getTimeZone(TIMEZONE_AMERICA_TORONTO));
		//System.out.println("Default TimeZone ==>" + TimeZone.getDefault().getID());
		Calendar serverTime = Calendar.getInstance(TimeZone.getDefault());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		System.out.println(dateFormat.format(serverTime.getTime()));
		return dateFormat.format(serverTime.getTime());
	}
	
	private HashMap<String,String> initializeAppConfigMappings(){
		HashMap<String,String> appUrlMappings = new HashMap<String, String>();
		for (String envVar : appEnvMappings) {
			appUrlMappings.put(envVar, System.getenv(envVar));
		}
		System.out.println("ServiceNoticeServlet.initializeAppConfigMappings() :: " + appUrlMappings);
		return appUrlMappings;
		
	}
}
