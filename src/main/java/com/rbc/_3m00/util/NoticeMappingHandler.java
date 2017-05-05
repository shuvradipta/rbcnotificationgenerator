package com.rbc._3m00.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.rbc._3m00.dto.Notice;

public class NoticeMappingHandler {
	
	private static final int NOTICE_MAPPING_COLUMN_COUNT = 5;
	private static NoticeMappingHandler instance = new NoticeMappingHandler();
	
	private NoticeMappingHandler(){
		//private Constructor
	}
	
	public static NoticeMappingHandler getInstance(){
		return instance;
	}
	
	public ArrayList<Notice> initializeNoticeMapping(String filePath){
		ArrayList<Notice> notices = new ArrayList<Notice>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
			String fileReadLine = "";
			while( (fileReadLine = reader.readLine()) != null){
				String[] noticeMappings = fileReadLine.split(",");
				if(noticeMappings.length == NOTICE_MAPPING_COLUMN_COUNT){
					Notice notice = new Notice();
					notice.setKey(noticeMappings[0]);
					notice.setNoticeText(noticeMappings[1]);
					notice.setUrl(noticeMappings[2]);
					notice.setPublicInd(Boolean.parseBoolean(noticeMappings[3]));
					notice.setKioskInd(Boolean.parseBoolean(noticeMappings[4]));
					
					notices.add(notice);
				}else{
					System.out.println("NoticeMappingInitializer.initializeNoticeMapping():: This notice has input error = " + fileReadLine);
				}
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("ServiceNoticeServlet.readNoticeURLMapping() - File Not Found or IOException");
		}
		return notices;
	}
	

}
