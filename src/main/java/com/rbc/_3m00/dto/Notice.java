package com.rbc._3m00.dto;

import java.io.Serializable;

public class Notice implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7571592626361600046L;
	private String key = "";
	private String noticeText = "";
	private String url = "";
	private boolean publicInd = false;
	private String startTime = "";
	private String expiryTime = "";
	private boolean kioskInd = false;
	
	private boolean active = false;
	
	public String getNoticeText() {
		return noticeText;
	}
	public void setNoticeText(String noticeText) {
		this.noticeText = noticeText;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		StringBuffer noticeStringBuffer = new StringBuffer();
		noticeStringBuffer.append("Notice")
			.append("(\"")
			.append(this.noticeText)
			.append("\"").append(",").append("\"")
			.append(this.url)
			.append("\"").append(",")
			.append(this.publicInd)
			.append(",").append("\"")
			.append(this.startTime)
			.append("\"").append(",")
			.append("\"")
			.append(this.expiryTime)
			.append("\"").append(",")
			.append(this.kioskInd)
			.append(")").append(";");
		return noticeStringBuffer.toString();
	}
	public Notice() {
		super();
	}
	public boolean isPublicInd() {
		return publicInd;
	}
	public void setPublicInd(boolean isPublic) {
		this.publicInd = isPublic;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}
	public boolean isKioskInd() {
		return kioskInd;
	}
	public void setKioskInd(boolean isKiosk) {
		this.kioskInd = isKiosk;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
