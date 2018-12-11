package com.project.chat.gameroom;

import com.project.chat.util.MessageType;

import lombok.Data;

@Data
public class ChatMessage {
	private Integer gameroomId;
	private String writer;
	private String message;
	private MessageType type;
}
