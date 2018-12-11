package com.project.chat.gameroom;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
	
}
