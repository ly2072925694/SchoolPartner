package com.cpu.entity;

import java.io.Serializable;

/**
 * ֪ͨʵ���� 
 * @author ZJC
 *
 */
public class Notice implements Serializable{

	private String title;//����
	private String content;//֪ͨ����
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
