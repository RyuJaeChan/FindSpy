package com.project.chat.gameroom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.project.chat.user.Player;

import lombok.Data;

@Data
public class Gameroom {
	private Integer id;
	public final static Integer MAX_ROOM_SIZE = 2;
	// thread safe
	private List<Player> players = Collections.synchronizedList(new ArrayList<>());

	private AtomicInteger sequence = new AtomicInteger(0);
	// private Set<String> userSet = Collections.synchronizedSet(new HashSet<>());

	private Timer timer = new Timer();

	public Gameroom() {

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

	public void setWord(String word1, String word2) {
		initSequence();
		players.stream().forEach(element -> {
			element.setVote(0);
		});
		// Random random = new Random();

		players.stream().forEach(element -> {
			element.setWord(word1);
		});

		players.get(0).setWord(word2);

		System.out.println("단어를 받은 " + players);
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
		System.out.println("seq in check : " + sequence.intValue());
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

	// 순서 섞는거 추가해야함
	public Player getCurrentUser() {
		System.out.println("getCurrentUserCalled????");
		Integer i = sequence.getAndIncrement();
		System.out.println("index : " + i);
		Player ret = players.get(i);
		System.out.println("getCurrentUserCalled : ret val : " + ret);
		/*
		 * if(sequence.intValue() < 0 || sequence.intValue() >= players.size()) { return
		 * null; }
		 */
		return ret;
	}

	public void votePlayer(String userName) {
		players.stream().filter(element -> userName.equals(element.getUserName())).findFirst().get().addVote();
	}

	public String getSelectedPlayer() {
		List<Player> sortedList = players.stream().sorted((a, b) -> b.compareTo(a)).collect(Collectors.toList());
		System.out.println("players in getselectedpalyer : " + sortedList);
		if (sortedList.get(0).getVote() == sortedList.get(1).getVote()) {
			return null;
		}
		return sortedList.get(0).getUserName();
	}

}
