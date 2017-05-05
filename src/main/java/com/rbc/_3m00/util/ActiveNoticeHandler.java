package com.rbc._3m00.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import com.rbc._3m00.dto.Notice;

public class ActiveNoticeHandler {
	
	private static ActiveNoticeHandler instance = new ActiveNoticeHandler();
	
	private static ArrayList<Notice> activeNotices = new ArrayList<Notice>();
	
	private ActiveNoticeHandler(){
		//private Constructor
	}
	
	public static ActiveNoticeHandler getInstance(){
		return instance;
	}
	
	public boolean updateActiveNotices(ArrayList<Notice> notices, String filePath){
		
		//append the new notices to the existing one
		for (Iterator<Notice> iterator = notices.iterator(); iterator.hasNext();) {
			Notice notice = (Notice) iterator.next();
			ActiveNoticeHandler.activeNotices.add(notice);
		}
		
		System.out.println("ActiveNoticeHandler.updateActiveNotices() - List of Notices after update :: " + ActiveNoticeHandler.activeNotices);
		try {
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(ActiveNoticeHandler.activeNotices);
			os.flush();
			os.close();
		} catch (IOException e) {
			System.out.println("ActiveNoticeHandler.updateActiveNotices() - IOException while closing writer"  + e.getMessage());
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Notice> readActiveNotices(String filePath){
		try {
			FileInputStream fis = new FileInputStream(new File(filePath));
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			ActiveNoticeHandler.activeNotices = (ArrayList<Notice>)ois.readObject();
			//System.out.println(notices);
			ois.close();
		} catch (IOException e) {
			System.out.println("ActiveNoticeHandler.readActiveNotices() - IOException while closing reader"  + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("ActiveNoticeHandler.readActiveNotices() - ClassNotFoundException while closing reader"  + e.getMessage());
		}
		return ActiveNoticeHandler.activeNotices;
	}

	public static ArrayList<Notice> getActiveNotices() {
		return activeNotices;
	}
	
	
}
