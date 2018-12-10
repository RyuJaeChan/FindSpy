package com.project.chat.gameroom;

import lombok.Data;

@Data
public class ChatMessage {
	private Integer gameroomId;
	private String writer;
	private String message;
}
