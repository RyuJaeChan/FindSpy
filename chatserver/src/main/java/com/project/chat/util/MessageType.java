package com.project.chat.util;

public enum MessageType {
	MESSAGE,			//chat message
	JOIN,
	QUIT,
	//Client -> Server
	GAME_REQUEST,		//game start request
	WORD_OK,			//user get word and response
	SELECT_OK,			//user select response 
	DESCRIPTION_FINISH,	//user's description finish message
	DESCRIPTION,		// description message : it is also sent by server 
	//Server -> Client message
	ALERT,				//server's anouncement
	GAME_START,			//game process start
	DESCRIPTION_START,	//description start message
	TIME_OUT,			//action time limit alert
	SELECT_START,		//suspect time start
	DESCRIPTION_RESTART,//the most voted player is not only one
	GAME_END			//game end ans send result
}
