package com.project.chat.gameroom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.project.chat.util.GameStep;

import lombok.Data;

@Data
public class Gameroom {
	private Integer id;
	final static Integer MAX_SIZE = 4;
	//thread safe
	private Set<String> players = Collections.synchronizedSet(new HashSet<>());
	private GameStep gameStep = GameStep.WAIT;
	private Set<String> userSet = Collections.synchronizedSet(new HashSet<>());
	private Timer timer = new Timer();
	
	@Autowired
	private SimpMessagingTemplate smt;
	
	public Gameroom() {
		
	}
	
	public Gameroom(Integer id) {
		this.id = id;
	}
	
	public boolean addUser(String e) {
		if(players.size() >= MAX_SIZE) {
			return false;
		}
		
		return players.add(e);
	}
	
	public boolean delUser(String e) {
		if(players.size() <= 0) {
			return false;
		}
		
		return players.remove(e);
	}
	
	public void gameProcess(GameMessage gameMessage) {
		
		switch(gameMessage.getGameStep()) {
		case CLIENT_START:
			
			break;
		case CLIENT_MESSAGE:
			
			break;
		case CLIENT_SELECT:
			
			break;
		default:
			break;
		}
		
		
		
		
		
	}
	
	
	public void startGame() {
		if(this.gameStep == GameStep.WAIT && this.userSet.size() == MAX_SIZE) {
			//game start!!
			
		}
		//smt.convertAndSend("/sub/gameroom/" + message.getGameroomId(), message);
	}
	
}
