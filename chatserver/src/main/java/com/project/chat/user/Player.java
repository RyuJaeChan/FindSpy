package com.project.chat.user;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Data;

@Data
public class Player implements Comparable<Player> {
	private String userName;
	private String word;
	private AtomicInteger vote = new AtomicInteger(0);

	public Player() {
	}

	public Player(String userName) {
		this.userName = userName;
	}

	public void addVote() {
		vote.incrementAndGet();
	}

	public Integer getVote() {
		return vote.intValue();
	}

	public void setVote(int val) {
		vote.set(val);
	}

	@Override
	public int compareTo(Player o) {
		return Integer.compare(vote.intValue(), o.getVote());
	}

}
