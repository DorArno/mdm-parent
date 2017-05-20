package com.test;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
	
	private String userName;
	
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUser(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
