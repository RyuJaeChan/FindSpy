package com.project.chat.gameroom;

import com.project.chat.util.GameStep;

import lombok.Data;

@Data
public class GameMessage {
	private Integer roomId;
	private GameStep gameStep;
	private String message;
	private String writer;
}
