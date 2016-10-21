package com.cpu.entity;

public class PublicInformation {
	private String UserName;
	private String Sex;
	private String PublishTime;
	private String ActivityTime;
	private String Title;
	private String PublicContent;
	private int Prove;
	private int Message;
	private int person;
	private int TypeName;
	private int data;
	private String Telephone;
	private int State;
	private int id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getState() {
		return State;
	}
	public void setState(int state) {
		State = state;
	}
	public String getTelephone() {
		return Telephone;
	}
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	public int getData() {
		return data;
	}
	public void setData(int data) {
		this.data = data;
	}
	public int getPerson() {
		return person;
	}
	public void setPerson(int person) {
		this.person = person;
	}
	public int getTypeName() {
		return TypeName;
	}
	public void setTypeName(int state) {
		this.TypeName = state;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getSex() {
		return Sex;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public String getPublishTime() {
		return PublishTime;
	}
	public void setPublishTime(String publishTime) {
		PublishTime = publishTime;
	}
	public String getActivityTime() {
		return ActivityTime;
	}
	public void setActivityTime(String activityTime) {
		ActivityTime = activityTime;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getPublicContent() {
		return PublicContent;
	}
	public void setPublicContent(String publicContent) {
		PublicContent = publicContent;
	}
	public int getProve() {
		return Prove;
	}
	public void setProve(int prove) {
		Prove = prove;
	}
	public int getMessage() {
		return Message;
	}
	public void setMessage(int message) {
		Message = message;
	}
	
}
