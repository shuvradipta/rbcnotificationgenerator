package com.rbc._3m00.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rbc._3m00.dto.Notice;
import com.rbc._3m00.util.ActiveNoticeHandler;
import com.rbc._3m00.util.NoticeMappingHandler;
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
	
	private static final String RESOURCES_ACTIVE_NOTICE_EN_TXT = "/resources/active_notices_en.txt";
	
	private static final String RESOURCES_ACTIVE_NOTICE_FR_TXT = "/resources/active_notices_fr.txt";

	private static final String SERVICE_NOTICE_JSP = "serviceNotice.jsp";

	private static final String SERVICENOTICE_JS_EN_PATH = "/js/servicenotice_en.js";
	
	private static final String SERVICENOTICE_JS_FR_PATH = "/js/servicenotice_fr.js";
	
	private static final String SERVICE_NOTICE_JS_INIT_FILE = "/js/servicenotice_init.js";

	private static final String PROCESSING_STATUS = "PROCESSING_STATUS";

	private static final long serialVersionUID = 1L;
	
	private static final String[] messageListTableColumnNames = new String[]{"Message", "Start Time","Expiry Time",/*"Kiosk",*/"Status", "Actions"};
	
	private static final String[] messageLeaderBoardTableColumnNames = new String[]{"Message", /*"URL","Public",*/"Start Time","Expiry Time",/*"Kiosk",*/"Add It?"};
	
	private static ArrayList<Notice> enNoticeMappings = null;
	
	private static ArrayList<Notice> frNoticeMappings = null;
	
	private static ArrayList<Notice> activeNoticeEnMappings = null;
	
	private static ArrayList<Notice> activeNoticeFrMappings = null;
	
	private static String[] appEnvMappings = {"HTMLGENAPPURL" , "SERVICENOTICEGENAPPURL", "MOBILEGENAPPURL"};
	
	private static HashMap<String,String> appConfigMappings =  null;
	
	private static HashMap<String, String> noticeMap = null;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServiceNoticeServlet() {
        super();
    }

	@Override
	public void init() throws ServletException {
		super.init();
		enNoticeMappings = NoticeMappingHandler.getInstance().initializeNoticeMapping(this.getServletContext().getRealPath(RESOURCES_NOTICE_URL_MAPPING_EN_TXT));
		frNoticeMappings = NoticeMappingHandler.getInstance().initializeNoticeMapping(this.getServletContext().getRealPath(RESOURCES_NOTICE_URL_MAPPING_FR_TXT));
		activeNoticeEnMappings = ActiveNoticeHandler.getInstance().readActiveNotices(this.getServletContext().getRealPath(RESOURCES_ACTIVE_NOTICE_EN_TXT));
		activeNoticeFrMappings = ActiveNoticeHandler.getInstance().readActiveNotices(this.getServletContext().getRealPath(RESOURCES_ACTIVE_NOTICE_FR_TXT));
		
		noticeMap = initializeNoticeMap();
		
		appConfigMappings = initializeAppConfigMappings();
	}

	private HashMap<String, String> initializeNoticeMap() {
		HashMap<String, String> noticeMap = new HashMap<String, String>();
		for (Iterator<Notice> iterator = enNoticeMappings.iterator(); iterator.hasNext();) {
			Notice notice = (Notice) iterator.next();
			noticeMap.put("en_"+ notice.getKey()+"url", notice.getUrl());
		}
		for (Iterator<Notice> iterator = frNoticeMappings.iterator(); iterator.hasNext();) {
			Notice notice = (Notice) iterator.next();
			noticeMap.put("fr_"+ notice.getKey()+"url", notice.getUrl());
		}
		//System.out.println("noticeMap :: " + noticeMap);
		return noticeMap;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(null != activeNoticeEnMappings)
			activeNoticeEnMappings = processActiveNoticesStatus(activeNoticeEnMappings);
		populateRequestAttributes(request);
		request.getRequestDispatcher(SERVICE_NOTICE_JSP).forward(request, response);
	}

	private void populateRequestAttributes(HttpServletRequest request) {
		//Initialize English and French frequent notices
		request.setAttribute("enNoticeMappings", enNoticeMappings);
		request.setAttribute("frNoticeMappings", frNoticeMappings);
		request.setAttribute("serverDateTime", getServerDateTime());
		request.setAttribute("messageListTableColumnNames", messageListTableColumnNames);
		request.setAttribute("messageLeaderBoardTableColumnNames", messageLeaderBoardTableColumnNames);
		
		request.setAttribute("appConfigMappings", appConfigMappings);
		request.setAttribute("noticeMap", noticeMap);
		request.setAttribute("activeNoticeEnMappings", activeNoticeEnMappings);
		request.setAttribute("activeNoticeFrMappings", activeNoticeFrMappings);
		
	}

	/**
	 * @throws IOException 
	 * @throws ServletException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		
		
		if( "delete".equalsIgnoreCase(request.getParameter("action")) ){
			doDelete(request, response);
		}else{
			activeNoticeEnMappings = appendToActiveNotices(activeNoticeEnMappings,extractNoticesFromRequest(request, enNoticeMappings));
			activeNoticeFrMappings = appendToActiveNotices(activeNoticeFrMappings,extractNoticesFromRequest(request, frNoticeMappings));
		}
		
		boolean processingSuccessfulEn = writeToFile(ServiceNoticeJSGenerator.getInstance().generateCode(activeNoticeEnMappings, this.getServletContext().getRealPath(SERVICE_NOTICE_JS_INIT_FILE)), SERVICENOTICE_JS_EN_PATH);
		boolean processingSuccessfulFr = writeToFile(ServiceNoticeJSGenerator.getInstance().generateCode(activeNoticeFrMappings, this.getServletContext().getRealPath(SERVICE_NOTICE_JS_INIT_FILE)), SERVICENOTICE_JS_FR_PATH);
		
		ActiveNoticeHandler.getInstance().updateActiveNotices(activeNoticeEnMappings, this.getServletContext().getRealPath(RESOURCES_ACTIVE_NOTICE_EN_TXT));
		ActiveNoticeHandler.getInstance().updateActiveNotices(activeNoticeFrMappings, this.getServletContext().getRealPath(RESOURCES_ACTIVE_NOTICE_FR_TXT));
		
		request.setAttribute(PROCESSING_STATUS, processingSuccessfulEn && processingSuccessfulFr);
		
		doGet(request, response);
	}
				

	private ArrayList<Notice> extractNoticesFromRequest(HttpServletRequest request, ArrayList<Notice> noticeMappings){
		ArrayList<Notice> notices = new ArrayList<Notice>();
		String noticeKeyList = formatJSArray(request.getParameter("noticeKeyList").trim());
		//System.out.println("ServiceNoticeServlet.extractNoticesFromRequest() - noticeKeyList ::" + noticeKeyList);
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
		//System.out.println("ServiceNoticeServlet.extractNoticesFromRequest():\n" + notices);
		return notices;
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String noticeKey = req.getParameter("key");
		Notice removalNotice = null;
		if(null != noticeKey && !"".equals(noticeKey.trim())) {
			removalNotice = new Notice();
			removalNotice.setKey(noticeKey);
		}
		boolean enDeleteSuccessful = ActiveNoticeHandler.getInstance().deleteActiveNotices(activeNoticeEnMappings, removalNotice, this.getServletContext().getRealPath(RESOURCES_ACTIVE_NOTICE_EN_TXT));
		boolean frDeleteSuccessful = ActiveNoticeHandler.getInstance().deleteActiveNotices(activeNoticeFrMappings, removalNotice, this.getServletContext().getRealPath(RESOURCES_ACTIVE_NOTICE_FR_TXT));
		
		if(!enDeleteSuccessful || !frDeleteSuccessful){
			System.out.println("ServiceNoticeServlet.doDelete() - some issue while trying to delete " + noticeKey);
		}
	}

	private ArrayList<Notice> appendToActiveNotices(ArrayList<Notice> activeNotices, ArrayList<Notice> newNotices){
		//append the new notices to the existing one
		if(activeNotices == null) {
			activeNotices = new ArrayList<Notice>();
		}
		for (Iterator<Notice> iterator = newNotices.iterator(); iterator.hasNext();) {
			Notice notice = (Notice) iterator.next();
			if(!activeNotices.contains(notice)) { // Don't add duplicates
				activeNotices.add(notice);
			}
		}
		System.out.println("ServiceNoticeServlet.appendToActiveNotices() - updated list :: " + activeNotices);
		return activeNotices;
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
		//System.out.println("formatDateTime = " + dateTime);
		return dateTime;
	}
	
	private String getServerDateTime(){
		TimeZone.setDefault(TimeZone.getTimeZone(TIMEZONE_AMERICA_TORONTO));
		//System.out.println("Default TimeZone ==>" + TimeZone.getDefault().getID());
		Calendar serverTime = Calendar.getInstance(TimeZone.getDefault());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		//System.out.println(dateFormat.format(serverTime.getTime()));
		return dateFormat.format(serverTime.getTime());
	}
	
	private HashMap<String,String> initializeAppConfigMappings(){
		HashMap<String,String> appUrlMappings = new HashMap<String, String>();
		for (String envVar : appEnvMappings) {
			appUrlMappings.put(envVar, System.getenv(envVar));
		}
		//System.out.println("ServiceNoticeServlet.initializeAppConfigMappings() :: " + appUrlMappings);
		return appUrlMappings;
		
	}
	
	private ArrayList<Notice> processActiveNoticesStatus(ArrayList<Notice> notices){
		Calendar serverTime = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_AMERICA_TORONTO));
		Date serverTimeDate = serverTime.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date noticeStartTime = null;
		Date noticeExpiryTime = null;
		for (Iterator<Notice> iterator = notices.iterator(); iterator.hasNext();) {
			Notice notice = (Notice) iterator.next();
			try {
				noticeStartTime = dateFormat.parse(notice.getStartTime());
				noticeExpiryTime = dateFormat.parse(notice.getExpiryTime());
			} catch (ParseException e) {
				System.out.println("ServiceNoticeServlet.processActiveNoticesStatus() - ParseException" + e.getMessage());
			}
			if(serverTimeDate.after(noticeStartTime) && serverTimeDate.before(noticeExpiryTime)){
				notice.setStatus("green");
			}else if(serverTimeDate.before(noticeStartTime)){
				notice.setStatus("grey");
			}else{
				notice.setStatus("red");
			}
		}
		return notices;
	}
}
