package com.test.mytest.dto;


import lombok.Getter;

@Getter
public class WebSocketResponse {
    private String content;

    public WebSocketResponse(String content) {
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
