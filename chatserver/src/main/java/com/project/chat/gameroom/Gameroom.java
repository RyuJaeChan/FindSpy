package com.project.chat.gameroom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.project.chat.user.Player;
import com.project.chat.util.GameState;

import lombok.Data;

@Data
public class Gameroom {
	private Integer id;
	public final static Integer MAX_ROOM_SIZE = 3;
	// thread safe
	private List<Player> players = Collections.synchronizedList(new ArrayList<>());
	private List<String> check = Collections.synchronizedList(new ArrayList<>());
	private Player currentPlayer;	//current player having description turn
	private Player suspectedPlayer;	//player getting a different word
	private GameState gameState = GameState.CHATTING;

	private AtomicInteger sequence = new AtomicInteger(0);

	private Timer timer = new Timer();
	
	Random random = new Random();

	public Gameroom() {

	}
	
	/*
	 *	게임 시작할 떄 초기화
	 */
	public void init() {
		initSequence();
		
		//set voted value 0
		players.stream().forEach(element -> {
			element.setVote(0);
		});
		
		check.clear();
		//shuffle Players List
		Collections.shuffle(players);
	}

	public Gameroom(Integer id) {
		this.id = id;
	}

	public boolean addUser(String e) {
		if (players.size() >= MAX_ROOM_SIZE) {
			return false;
		}

		return players.add(new Player(e));
	}

	public boolean delUser(String userName) {
		if (players.size() <= 0) {
			return false;
		}

		return players.removeIf(element -> userName.equals(element.getUserName()));
	}

	
	
	//Return Player name getting a different word
	public String setWord(String word1, String word2) {
		gameState = GameState.WORD_WAITING;

		int index = random.nextInt(MAX_ROOM_SIZE);

		players.stream().forEach(element -> {
			element.setWord(word1);
		});

		players.get(index).setWord(word2);
		
		suspectedPlayer = players.get(index); 

		System.out.println("단어를 받은 " + players);
		return suspectedPlayer.getUserName();
	}

	public String getWord(String userName) {
		Optional<Player> word = players.stream().filter(element -> userName.equals(element.getUserName())).findFirst();
		return word.get().getWord();
	}

	private void initSequence() {
		sequence.set(0);
	}

	// 여기서 문제가 될 수도 있음
	/*
	 * 
	 */
	synchronized public boolean checkAllPlayerFinish() {
		if (sequence.intValue() == players.size()) {
			System.out.println("check return true");
			initSequence();
			return true;
		} else {
			System.out.println("check return flase");
			return false;
		}
	}

	public void addSequence() {
		sequence.getAndIncrement();
		System.out.println("seq in add sequence : " + sequence.intValue());
	}

	// return player having description turn now
	public Player getCurrentUser() {
		System.out.println("getCurrentUserCalled????");
		Integer i = sequence.getAndIncrement();
		System.out.println("index : " + i);
		currentPlayer = players.get(i);
		System.out.println("getCurrentUserCalled : ret val : " + currentPlayer);
		/*
		 * if(sequence.intValue() < 0 || sequence.intValue() >= players.size()) { return
		 * null; }
		 */
		return currentPlayer;
	}

	public boolean votePlayer(String fromUserName, String userName) {
		boolean checkAlreadyVote = check.stream().anyMatch(element -> element.equals(fromUserName));
		if(checkAlreadyVote) {
			return false;
		}
		
		addSequence();
		players.stream().filter(element -> userName.equals(element.getUserName())).findFirst().get().addVote();
		check.add(fromUserName);
		
		return true;
	}

	//if same voted player, return null;
	public String getSelectedPlayer() {
		List<Player> sortedList = players.stream().sorted((a, b) -> b.compareTo(a)).collect(Collectors.toList());
		System.out.println("players in getselectedpalyer : " + sortedList);
		if (sortedList.get(0).getVote() == sortedList.get(1).getVote()) {
			return null;
		}
		return sortedList.get(0).getUserName();
	}

}
