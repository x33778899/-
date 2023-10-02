package com.test.mytest.controller;

import java.util.HashMap;
import java.util.Map;

import com.test.mytest.dto.PrivateMessage;
import com.test.mytest.dto.UserMessage;
import com.test.mytest.dto.WebSocketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
public class WebSocketController {

	private final SimpMessagingTemplate messagingTemplate;

	@Autowired
	public WebSocketController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	// 公開頻道
	@MessageMapping("/public")
	@SendTo("/topic/user")
	public WebSocketResponse getUser(UserMessage user, @RequestParam("token") String token) {
		String message = user.getUsername() + " : " + user.getMessage();
		return new WebSocketResponse(message);
	}

	private final ObjectMapper objectMapper = new ObjectMapper();

	// 私人頻道
	@MessageMapping("/private")
	public void sendPrivateMessage(PrivateMessage privateMessage) {
		String senderUsername = privateMessage.getSenderUsername();
		String recipientUsername = privateMessage.getRecipientUsername();
		String messageContent = privateMessage.getMessage();

		// 產生json 格式的物件
		Map<String, String> message = new HashMap<>();
		message.put("senderUsername", senderUsername);
		message.put("content", messageContent);

		// Convert the message map to JSON
		String jsonMessage;
		try {
			jsonMessage = objectMapper.writeValueAsString(message);
			System.out.println("jsonMessage	" + jsonMessage);
		} catch (JsonProcessingException e) {
			System.err.println("Error converting message to JSON: " + e.getMessage());
			return;
		}

		System.out.println(
				"Private Message: From " + senderUsername + " to " + recipientUsername + ": " + messageContent);

		// Send the private message to the recipient's WebSocket
		String recipientTopic = "/topic/private/" + recipientUsername;
		messagingTemplate.convertAndSend(recipientTopic, jsonMessage);

		// Send the private message to the sender's WebSocket
		String senderTopic = "/topic/private/" + senderUsername;
		messagingTemplate.convertAndSend(senderTopic, jsonMessage);
	}

}
