package com.project.chat.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.chat.gameroom.ChatMessage;
import com.project.chat.gameroom.Gameroom;
import com.project.chat.gameroom.GameroomRepository;


//https://www.baeldung.com/websockets-spring

//https://supawer0728.github.io/2018/03/30/spring-websocket/

@Controller
public class GameController {
	/*
	@MessageMapping("/add")
	@SendTo("/topic/messages")
	public String send(String message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
		System.out.println("message : " + message);
		System.out.println("headerAccessor : " + headerAccessor);
		
	    return message;
	}
	*/
	
	private GameroomRepository gameroomRepository = new GameroomRepository();
	
	private SimpMessagingTemplate s;
	
	//게임방 조회
	@GetMapping("/games")
	public String games(ModelMap modelMap) {
		List<Gameroom> list = new ArrayList<>(gameroomRepository.getGamerooms());
		modelMap.put("list", list);
		
		return "games";
	}
	
	//게임방 생성
	@PostMapping("/games")
	public String createGame() {
		Integer id = gameroomRepository.createGameroom();
		return "redirect:/gameroom/" + id.toString();
	}
	
	//게임방 참가
	/* 게임방에 참가할 수 없을 경우 
	 * 	- 정원이 가득참
	 * 	- 게임방이 존재하지 않음
	 * 
	 * 에 대한 예외처리 필요합니다.
	 */
	@GetMapping("/gameroom/{id}")
	public String gameroom(@PathVariable Integer id, ModelMap modelMap, Principal principal) {
		System.out.println("principal : " + principal);
		System.out.println("principal.getName() : " + principal.getName());
		Gameroom gameroom = gameroomRepository.getGameroom(id);
		
		
		boolean result =gameroomRepository.joinGameroom(id, principal.getName());
		
		modelMap.put("gameroom", gameroom);
		modelMap.put("result", result);
		
		return "gameroom";
	}
	
	//client에서 보낸 메시지를 처리한다.
	//config에서 setApplicationDestinationPrefixes를 통해 등록한 url/send 이런식으로 클라이언트에서 보낸다.
	@MessageMapping("/send")
	public String s(ChatMessage message, SimpMessageHeaderAccessor headerAccessor, Principal princ) {
		System.out.println("chatmessage : " + message);
		System.out.println("headerAccessor : " + headerAccessor);
		System.out.println("pric : " + princ);
		s.convertAndSend("/sub/");
		return "send something";
	}
	
	
	//해당 게임방에 참가
	@SendTo("/sub/gameroom")
	public String gameroom(String message, SimpMessageHeaderAccessor headerAccessor) {
		System.out.println("message : " + message);
		System.out.println("headerAccessor : " + headerAccessor);
		return "gameroom";
	}
	
}
