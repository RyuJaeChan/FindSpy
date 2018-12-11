package com.project.chat.util;

public enum GameStep {
	//Server -> Client message
	WAIT,
	GMAE_START,
	MESSAGE_START,
	TIME_OUT,
	SELECT_START,
	RESULT,
	//Client -> Server message
	CLIENT_START,	//client's Game start message
	CLIENT_MESSAGE,
	CLIENT_SELECT
}
