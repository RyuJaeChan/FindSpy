package com.project.chat.gameroom;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.project.chat.util.MessageType;

import static com.project.chat.gameroom.Gameroom.MAX_ROOM_SIZE;

@Component
public class GameroomRepository {
	//Thread-Safe
	private ConcurrentHashMap<Integer, Gameroom> gamerooms = new ConcurrentHashMap<>();
	private final AtomicInteger seq = new AtomicInteger(0);
	
	public String getWord(Integer gameroomId, String userName) {
		return gamerooms.get(gameroomId).getWord(userName);
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
		Gameroom gameroom = gamerooms.get(gameroomId);
		switch(message.getMessageType()) {
		case GAME_REQUEST:
			//인원이 4명 모였으면 단어 분배 및 시작
			//
			//단어 생성 및 분배
			//
			boolean result = startGame(gameroomId);
			if(result == false) {
				message.setMessage("게임 인원이 부족하여 시작할 수 없습니다.");
				message.setMessageType(MessageType.ALERT);
				break;
			}
			
			//GAME_START
			message.setMessageType(MessageType.GAME_START);
			break;
		case WORD_OK:
			gameroom.addSequence();
			if(gameroom.checkAllPlayerFinish()) {
				System.out.println("word_ok is true");
				System.out.println("gameroom : " + gameroom);
				
				message.setMessage(
						gameroom
						.getCurrentUser()
						.getUserName());
				//send DESCRIPTION_START
				message.setMessageType(MessageType.DESCRIPTION_START);
			}
			break;
		case DESCRIPTION:
			//do nothing
			break;
		case DESCRIPTION_FINISH:
			if(gameroom.checkAllPlayerFinish()) {	//모두 설명을 완료했으면 범인 지목 시작
				message.setMessageType(MessageType.SELECT_START);
			}
			else {
				//다음 사람 설명 시작
				message.setMessage(gameroom
									.getCurrentUser()
									.getUserName());
				message.setMessageType(MessageType.DESCRIPTION_START);
			}
			
			break;
		case SELECT_OK:
			//4명 모두 선택 완료?
			//결과 전송
			//send RESULT
			if(gameroom.checkAllPlayerFinish()) {
			
				message.setMessageType(MessageType.RESULT);
			}
			else {
				gameroom.votePlayer(message.getMessage());
			}
			break;
		default:
			//error
			System.out.println("== NOT DEFINED MESSAGE TYPE ==");
			break;
		}
		
	}
	
	private boolean startGame(Integer gameroomId) {
		if(gamerooms.get(gameroomId).getPlayers().size() < MAX_ROOM_SIZE) { 
			//can't start message
			return false;
		}
		
		setWordList(gameroomId);
		return true;
	}
	
	private void setWordList(Integer gameroomId) {
		//jdbc에서 단어쌍 가져오기
		/*
		 * 로직 추가
		 * 
		 * */
		String a = "라이터";
		String b = "부싯돌";
		
		gamerooms.get(gameroomId).setWord(a, b);
	}
	
	
}
