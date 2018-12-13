package com.project.chat.gameroom;

import com.project.chat.util.MessageType;

import lombok.Data;

@Data
public class ChatMessage {
	//private Integer gameroomId;
	private String writer;
	private String message;
	private MessageType messageType;
	
	public ChatMessage() {}
	
	private ChatMessage(Builder builder) {
		//this.gameroomId = builder.gameroomId;
		this.writer = builder.writer;
		this.message = builder.message;
		this.messageType = builder.type;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		//private Integer gameroomId;
		private String writer;
		private String message;
		private MessageType type;
		
		public ChatMessage build() {
			return new ChatMessage(this);
		}
		
		/*
		public Builder setGameroomId(Integer id) {
			this.gameroomId = id;
			return this;
		}
		*/
		public Builder setWriter(String writer) {
			this.writer = writer;
			return this;
		}
		
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}
		
		public Builder setType(MessageType type) {
			this.type = type;
			return this;
		}
		
	}
	
}
