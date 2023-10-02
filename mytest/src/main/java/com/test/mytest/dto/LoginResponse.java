package com.test.mytest.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
	// 属性和getter/setter方法

//	@JsonProperty("status_code")
	private int statusCode;
//	@JsonProperty("token")
	private String token;

	private Map<String, Object> data;
//	@JsonProperty("message")
	private String message;

//	@JsonProperty("login_success")
	private boolean loginSuccess;

	public Map<String, Object> getData() {
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