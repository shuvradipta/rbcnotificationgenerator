package com.rbc._3m00.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.rbc._3m00.dto.Notice;

public class ServiceNoticeJSGenerator {
	
	private static String NUMBER_OF_NOTICES_LOC = "numberofnotices=";
	
	private static String NOTICES_NEW_ARRAY_LOC = "notices = new Array(numberofnotices)";
	
	private static String LOC_END_DELIMITER = ";";
	
	private static String WEEKEND_MAINT_COMMENT_LOC = "// WEEKEND MAINTENANCE";
	
	private static String NOTICES_OBJECT = "notices";
	
	private static String NOTICES_OBJECT_ARRAY_OPEN = "[";
	
	private static String NOTICES_OBJECT_ARRAY_CLOSE = "]";
	
	private static String LOC_EQUAL = " = ";
	
	private static String LOC_NEW = "new ";
	
	private static ServiceNoticeJSGenerator instance = new ServiceNoticeJSGenerator();
	
	public static ServiceNoticeJSGenerator getInstance(){
		return instance;
	}
	
	private ServiceNoticeJSGenerator(){
		super();
	}
	
	public String generateCode(ArrayList<Notice> notices, String filePath){
		System.out.println("File Path = " + filePath);
		StringBuffer codeBuffer = initFile(filePath);
		if(codeBuffer != null) {
			codeBuffer.append("\n" + NUMBER_OF_NOTICES_LOC + notices.size() + LOC_END_DELIMITER + "\n");
			codeBuffer.append("\n" + NOTICES_NEW_ARRAY_LOC + LOC_END_DELIMITER + "\n");
			codeBuffer.append("\n" + WEEKEND_MAINT_COMMENT_LOC + "\n");
			int counter = 0;
			for (Iterator<Notice> iterator = notices.iterator(); iterator.hasNext();) {
				Notice notice = (Notice) iterator.next();
				codeBuffer.append("\n" + NOTICES_OBJECT+NOTICES_OBJECT_ARRAY_OPEN+counter+NOTICES_OBJECT_ARRAY_CLOSE + LOC_EQUAL + LOC_NEW + notice.toString());
				counter++;
			}
		}
		
		System.out.println("JSGenerator.generateCode():\n" + codeBuffer.toString());
		return codeBuffer.toString();
	}
	
	private StringBuffer initFile(String filePath) {
		BufferedReader initFileReader;
		StringBuffer initializedCode = new StringBuffer();
		try {
			initFileReader = new BufferedReader(new FileReader(new File(filePath)));
			String fileReadLine = "";
			try {
				while((fileReadLine = initFileReader.readLine()) != null){
					initializedCode.append(fileReadLine + "\n");
				}
				initFileReader.close();
			} catch (IOException e) {
				System.out.println("ServiceNoticeJSGenerator.initFile() - IOException " + e.getMessage());
				try {
					initFileReader.close();
				} catch (IOException e1) {
					System.out.println("ServiceNoticeJSGenerator.initFile() - IOException while closing initFileReader" + e.getMessage());
				}
			}
		} catch (FileNotFoundException e1) {
			System.out.println("ServiceNoticeJSGenerator.initFile() - File Not Found... ");
		}
		System.out.println("ServiceNoticeJSGenerator.initFile() :: \n" + initializedCode);
		return initializedCode;
	}
	
}
