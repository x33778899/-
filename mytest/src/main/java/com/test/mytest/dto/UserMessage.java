package com.test.mytest.dto;


public class UserMessage {

    private String username; // Make sure the field name matches the property sent from the frontend
    private String message;



	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


}