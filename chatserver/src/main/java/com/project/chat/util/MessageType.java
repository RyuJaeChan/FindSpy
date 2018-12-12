package com.project.chat.util;

public enum MessageType {
	MESSAGE,			//chat message
	//Client -> Server
	GAME_REQUEST,		//game start request
	WORD_OK,			//user get word and response
	SELECT_OK,			//user select response 
	DESCRIPTION,
	//Server -> Client message
	ALERT,				//server's anouncement
	GAME_START,			//game process start
	DESCRIPTION_START,	//description start message
	TIME_OUT,			//action time limit alert
	SELECT_START,		//suspect time start
	RESULT				//game result
}
