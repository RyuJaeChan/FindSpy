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
	private List<String> players = Collections.synchronizedList(new ArrayList<>());
	
	private Integer sequence = 0;
	//private Set<String> userSet = Collections.synchronizedSet(new HashSet<>());
	private Timer timer = new Timer();
	
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
	
	public void initSequence() {
		sequence = 0;
	}
	
	public String getCurrentUser() {
		if(sequence < 0 || sequence > players.size()) {
			return "";
		}
		
		return players.get(sequence++);
	}
	
	
}
