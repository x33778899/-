package com.test.mytest.dto;


import java.util.Map;

public class LoginResponse {


	private int statusCode;
	private String token;

	private Map<String, Object> data;

	private String message;


	private boolean loginSuccess;

	public Object getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}

	public boolean isLoginSuccess() {
		return loginSuccess;
	}


	// 其他属性...
}