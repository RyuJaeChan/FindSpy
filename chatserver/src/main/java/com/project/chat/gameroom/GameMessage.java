package com.project.chat.gameroom;

import com.project.chat.util.GameState;

import lombok.Data;

@Data
public class GameMessage {
	private Integer roomId;
	private GameState gameStep;
	private String message;
	private String writer;
}
