package com.project.chat.gameroom;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class GameroomRepository {
	//Thread-Safe
	private ConcurrentHashMap<Integer, Gameroom> gamerooms = new ConcurrentHashMap<>();
	private final AtomicInteger seq = new AtomicInteger(0);
	
	public Integer createGameroom() {
		Integer id = seq.incrementAndGet();
		gamerooms.put(id, new Gameroom(id));
		return id;
	}
	
	public Collection<Gameroom> getGamerooms() {
		return gamerooms.values();
	}
	
	public Gameroom getGameroom(Integer id) {
		return gamerooms.get(id);
	}
	
	public boolean joinGameroom(Integer id, String userId) {
		Gameroom gameroom = gamerooms.get(id);
		return gameroom.addUser(userId);
	}
	
	public boolean quitGameroom(Integer id, String userId) {
		Gameroom gameroom = gamerooms.get(id);
		gameroom.delUser(userId);
		if(gameroom.getPlayers().size() == 0) {
			gamerooms.remove(id);
		}
		return true;
	}
	
	public void gameProcess(ChatMessage message) {
		
		
		
		
	}
	
	
}
