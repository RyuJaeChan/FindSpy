package com.project.chat.gameroom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.project.chat.user.AuthUser;

import lombok.Data;

@Data
public class Gameroom {
	private Integer id;
	final static Integer MAX_SIZE = 4;
	//thread safe
	private List<AuthUser> users = Collections.synchronizedList(new ArrayList<>());
	
	public Gameroom() {
		
	}
	
	public Gameroom(Integer id) {
		this.id = id;
	}
	
	public boolean add(AuthUser e) {
		if(users.size() >= MAX_SIZE) {
			return false;
		}
		
		return users.add(e);
	}
	
	public boolean del(AuthUser e) {
		if(users.size() <= 0) {
			return false;
		}
		
		return users.remove(e);
	}
	
}
