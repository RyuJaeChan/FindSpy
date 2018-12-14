package com.project.chat.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.chat.gameroom.ChatMessage;
import com.project.chat.gameroom.GameMessage;
import com.project.chat.gameroom.Gameroom;
import com.project.chat.gameroom.GameroomRepository;
import com.project.chat.user.AuthUser;
import com.project.chat.util.MessageType;


//https://www.baeldung.com/websockets-spring

//https://supawer0728.github.io/2018/03/30/spring-websocket/

@Controller
public class GameController {	
	@Autowired
	private GameroomRepository gameroomRepository;
	
	@Autowired
	private SimpMessagingTemplate smt;
	
	//게임방 조회
	@GetMapping("/games")
	public String games(ModelMap modelMap, @AuthenticationPrincipal AuthUser user) {
		System.out.println("games : authUser : " + user);
		
		AuthUser u = (AuthUser)SecurityContextHolder
		        .getContext()
		        .getAuthentication()
		        .getPrincipal();
		System.out.println("uuuu : " + u);
		
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
		
		System.out.println("gameroom : " + gameroom);
		
		modelMap.put("gameroom", gameroom);
		modelMap.put("userId", principal.getName());
		modelMap.put("roomid", gameroom.getId());
		
		return "gameroom";
	}
	
	@GetMapping("/words")
	@ResponseBody
	public Map<String, String> keyWord(@AuthenticationPrincipal AuthUser user) {
		System.out.println("/words user : " + user);
		String word = gameroomRepository.getWord(user.getGameroomId(), user.getUsername());
		System.out.println("user's : " + user.getUsername() + " 's word : "+ word);
		
		return Collections.singletonMap("word", word);
	}
	
	//client에서 보낸 메시지를 처리한다.
	//config에서 setApplicationDestinationPrefixes를 통해 등록한 url/send 이런식으로 클라이언트에서 보낸다.
	// 메시지 응답은 /sub/gameroom/{roomid}" 이런식으로 하면 된다.
	@MessageMapping("/message")
	public void sendMessage(ChatMessage message, @AuthenticationPrincipal AuthUser user) {
		System.out.println("message chatmessage : " + message);
		System.out.println("message user : " + user);
		
		smt.convertAndSend("/sub/gameroom/" + user.getGameroomId(), message);
	}
	
	@MessageMapping("/join")
	public void joinMessage(ChatMessage message, @AuthenticationPrincipal AuthUser user) {
		System.out.println("join recv message : " + message);
		System.out.println("join user : " + user);
		//boolean result = gameroomRepository.joinGameroom(message.getGameroomId(), user.getUsername());
		
		Integer roomId = Integer.parseInt(message.getMessage());
		
		boolean result = gameroomRepository.joinGameroom(roomId, user.getUsername());
		
		user.setGameroomId(roomId);
		
		message.setMessageType(MessageType.JOIN);
		message.setMessage(user.getUsername() + "님이 참가하였습니다.");
		
		System.out.println("join message : " + message);
		smt.convertAndSend("/sub/gameroom/" + roomId, message);
	}
	
	@MessageMapping("/quit")
	public void quitMessage(@AuthenticationPrincipal AuthUser user) {
		gameroomRepository.quitGameroom(user.getGameroomId(), user.getUsername());

		ChatMessage message = ChatMessage
									.builder()
									.setType(MessageType.QUIT)
									.setWriter(user.getUsername())
									.setMessage(user.getUsername() + "님이 퇴장하였습니다.")
									.build();
		
		smt.convertAndSend("/sub/gameroom/" + user.getGameroomId(), message);
		
		user.setGameroomId(null);
	}
	
	@MessageMapping("/play")
	public void sendGameStep(ChatMessage message, @AuthenticationPrincipal AuthUser user) {
		System.out.println("play message : " + message);
		System.out.println("play user : " + user);
		
		gameroomRepository.gameProcess(user.getGameroomId(), message);
		
		System.out.println("play send message : " + message);
		smt.convertAndSend("/sub/gameroom/" + user.getGameroomId(), message);
	}

}
