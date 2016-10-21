package com.cpu.entity;

import java.io.Serializable;

/**
 * 通知实体类 
 * @author ZJC
 *
 */
public class Notice implements Serializable{

	private String title;//标题
	private String content;//通知内容
	public Notice(String strTitle,String strContent){
		this.title = strTitle;
		this.content = strContent;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
