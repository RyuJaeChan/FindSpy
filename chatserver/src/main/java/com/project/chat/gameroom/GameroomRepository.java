package com.project.chat.gameroom;

import static com.project.chat.gameroom.Gameroom.MAX_ROOM_SIZE;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.project.chat.util.GameState;
import com.project.chat.util.MessageType;

@Component
public class GameroomRepository {
	// Thread-Safe
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

	/*
	 * 해당 방에 참가 후 참가한 플레이어 목록을 리턴
	 */
	public String joinGameroom(Integer id, String userId) {
		Gameroom gameroom = gamerooms.get(id);

		gameroom.addUser(userId);

		String msg = gameroom.getPlayers().stream().map(e -> e.getUserName()).collect(Collectors.toList()).toString();

		return msg;
	}

	public String quitGameroom(Integer id, String userId) {
		Gameroom gameroom = gamerooms.get(id);
		gameroom.delUser(userId);

		if (gameroom.getPlayers().size() == 0) {
			gamerooms.remove(id);
			return "quit";
		}

		String msg = gameroom.getPlayers().stream().map(e -> e.getUserName()).collect(Collectors.toList()).toString();

		return msg;
	}

	//return false mean don't send message
	public boolean gameProcess(Integer gameroomId, ChatMessage message) {
		Gameroom gameroom = gamerooms.get(gameroomId);
		System.out.println("gameroom : " + gameroom);
		
		switch (message.getMessageType()) {
		case GAME_REQUEST:
			if(gameroom.getGameState() != GameState.CHATTING) {
				return false;
			}
			
			// 인원이 4명 모였으면 단어 분배 및 시작
			//
			// 단어 생성 및 분배
			//
			boolean result = startGame(gameroomId);
			if (result == false) {
				message.setMessage("인원이 부족하여 게임을 시작할 수 없습니다.");
				message.setMessageType(MessageType.ALERT);
				break;
			}
			
			gameroom.init();
			
			String playerList = gameroom.getPlayers().stream().map(e -> e.getUserName()).collect(Collectors.toList())
					.toString();

			// GAME_START
			message.setMessage(playerList);
			message.setMessageType(MessageType.GAME_START);
			gameroom.setGameState(GameState.WORD_WAITING);
			break;
		case WORD_OK:
			if(gameroom.getGameState() != GameState.WORD_WAITING) {
				message = null;
				return false;
			}
			
			gameroom.addSequence();
			if (gameroom.checkAllPlayerFinish()) {
				System.out.println("word_ok is true");
				System.out.println("gameroom : " + gameroom);

				message.setMessage(gameroom.getCurrentUser().getUserName());
				// send DESCRIPTION_START
				message.setMessageType(MessageType.DESCRIPTION_START);
				gameroom.setGameState(GameState.DESCRIPTION_WAITNG);
			}
			break;
		case DESCRIPTION:
			if(gameroom.getGameState() != GameState.DESCRIPTION_WAITNG 
			&& gameroom.getCurrentPlayer().getUserName() != message.getWriter()) {
				return false;
			}
			
			// do nothing
			break;
		case DESCRIPTION_FINISH:
			if(gameroom.getGameState() != GameState.DESCRIPTION_WAITNG 
					&& gameroom.getCurrentPlayer().getUserName() != message.getWriter()) {
				return false;
			}
			
			if (gameroom.checkAllPlayerFinish()) { // 모두 설명을 완료했으면 범인 지목 시작
				message.setMessageType(MessageType.SELECT_START);
				gameroom.setGameState(GameState.SELECT_WAITING);
			} else {
				// 다음 사람 설명 시작
				message.setMessage(gameroom.getCurrentUser().getUserName());
				message.setMessageType(MessageType.DESCRIPTION_START);
			}

			break;
		case SELECT_OK:
			System.out.println("gameroom.getGameState() : " + gameroom.getGameState());
			System.out.println("gameroom.getGameState() != GameState.SELECT_WAITING : " + (gameroom.getGameState() != GameState.SELECT_WAITING));
			if(gameroom.getGameState() != GameState.SELECT_WAITING) {
				System.out.println("get state != selected Waitting");
				return false;
			}
			
			// 모두 선택 완료?
			// 결과 전송
			// send RESULT
			if(!gameroom.votePlayer(message.getWriter(), message.getMessage())) {
				System.out.println("vote return null");
				return false;
			}

			if (gameroom.checkAllPlayerFinish()) {
				String selectedPlayerName = gameroom.getSelectedPlayer();
				System.out.println("selectedPlayerName : " + selectedPlayerName);
				if (selectedPlayerName == null) {
					message.setMessageType(MessageType.DESCRIPTION_RESTART);
					gameroom.setGameState(GameState.WORD_WAITING);
					gameroom.init();
					break;
				}
				
				
				String msg = "FAIL";
				if(selectedPlayerName.equals(gameroom.getSuspectedPlayer().getUserName())) {
					msg = "SUCCESS";
				}
				
				message.setWriter(selectedPlayerName);
				message.setMessage(msg);
				message.setMessageType(MessageType.GAME_END);
				gameroom.setGameState(GameState.CHATTING);
			}
			break;
		default:
			// error
			System.out.println("== NOT DEFINED MESSAGE TYPE ==");
			break;
		}
		return true;
	}

	private boolean startGame(Integer gameroomId) {
		if (gamerooms.get(gameroomId).getPlayers().size() < MAX_ROOM_SIZE) {
			// can't start message
			return false;
		}

		setWordList(gameroomId);
		return true;
	}

	private void setWordList(Integer gameroomId) {
		// jdbc에서 단어쌍 가져오기
		/*
		 * 로직 추가
		 * 
		 */
		String a = "라이터";
		String b = "부싯돌";

		gamerooms.get(gameroomId).setWord(a, b);
	}

}
