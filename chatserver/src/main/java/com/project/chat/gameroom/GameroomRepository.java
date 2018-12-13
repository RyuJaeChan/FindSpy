package com.project.chat.gameroom;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.project.chat.util.MessageType;

@Component
public class GameroomRepository {
	//Thread-Safe
	private ConcurrentHashMap<Integer, Gameroom> gamerooms = new ConcurrentHashMap<>();
	private final AtomicInteger seq = new AtomicInteger(0);
	
	final static Integer MAX_ROOM_SIZE = 4;
	
	private Random random = new Random();
	
	// key : user name, value : word
	private ConcurrentHashMap<String, String> words = new ConcurrentHashMap<>();
	
	public String getWord(String userName) {
		String word = words.get(userName);
		words.remove(userName);
		return word;
	}
	
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
	
	public void gameProcess(Integer gameroomId, ChatMessage message) {
		switch(message.getMessageType()) {
		case GAME_REQUEST:
			//인원이 4명 모였으면 단어 분배 및 시작
			//
			//단어 생성 및 분배
			//
			startGame(gameroomId);
			//GAME_START
			message.setMessageType(MessageType.GAME_START);
			break;
		case WORD_OK:
			
			//send DESCRIPTION_START
			Gameroom gameroom = gamerooms.get(gameroomId);
			message.setMessage(gameroom.getCurrentUser());
			message.setMessageType(MessageType.DESCRIPTION_START);
			break;
		case DESCRIPTION:
			
			
			break;
		case SELECT_OK:
			//4명 모두 선택 완료?
			//결과 전송
			
			//send RESULT
			break;
		default:
			//error
			System.out.println("NOT DEFINED MESSAGE TYPE");
			break;
		}
		
	}
	
	private void startGame(Integer gameroomId) {
		if(gamerooms.get(gameroomId).getPlayers().size() < MAX_ROOM_SIZE) { 
			//can't start message
		}
		
		setWordList(gameroomId);
		//send WORD_OK message
		
	}
	
	private void setWordList(Integer gameroomId) {
		//jdbc에서 단어쌍 가져오기
		/*
		 * 로직 추가
		 * 
		 * */
		String a = "라이터";
		String b = "부싯돌";
		
		List<String> l = gamerooms
			.get(gameroomId)
			.getPlayers();
		System.out.println("2=============word : " + l);
		for(String e : l) {
			words.put(e, a);
		}
		
		System.out.println("3=============word");
		/*
		String key = gamerooms
				.get(gameroomId)
				.getPlayers().get(random.nextInt(4));
		*/
		//int i = random.nextInt(0);
		int i = 0;
		System.out.println("4=============word");
		System.out.println("i : " + i);
		String key = l.get(i);
		
		words.put(key, b);
	}
	
	
}
