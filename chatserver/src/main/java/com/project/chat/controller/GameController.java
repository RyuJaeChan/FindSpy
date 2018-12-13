package com.project.chat.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
	/*
	@MessageMapping("/add")
	@SendTo("/topic/messages")
	public String send(String message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
		System.out.println("message : " + message);
		System.out.println("headerAccessor : " + headerAccessor);
		
	    return message;
	}
	*/
	
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
		
		modelMap.put("gameroom", gameroom);
		modelMap.put("userId", principal.getName());
		modelMap.put("roomid", gameroom.getId());
		
		return "gameroom";
	}
	
	@GetMapping("/words")
	public String keyWord(Principal principal) {
		
		
		
		return "result";
	}
	
	//client에서 보낸 메시지를 처리한다.
	//config에서 setApplicationDestinationPrefixes를 통해 등록한 url/send 이런식으로 클라이언트에서 보낸다.
	// 메시지 응답은 /sub/gameroom/{roomid}" 이런식으로 하면 된다.
	@MessageMapping("/message")
	public void sendMessage(ChatMessage message, @AuthenticationPrincipal AuthUser user) {
		System.out.println("message chatmessage : " + message);
		System.out.println("message user : " + user);
		
		if(message.getMessageType() == MessageType.DESCRIPTION) {
			Gameroom gameroom = gameroomRepository.getGameroom(user.getGameroomId());
			gameroom.getUserSet().add(user.getUsername());
		}
		
		smt.convertAndSend("/sub/gameroom/" + user.getGameroomId(), message);
	}
	
	/*
	 * @Secured(User.ROLE_USER)
@MessageMapping("/watch/{liveid}")
@SendTo("/topic/watchinfo-{liveid}")
@JsonView(View.Live.class)
public LiveWatchInfoMessage liveinfo(@DestinationVariable("liveid") String liveid,
                                         @AuthenticationPrincipal UserDetails activeUser) {
        ...
        return LiveWatchInfoMessage.builder().build();
    }
    */
	
	@MessageMapping("/join")
	public void joinMessage(ChatMessage message, @AuthenticationPrincipal AuthUser user) {
		System.out.println("join recv message : " + message);
		System.out.println("join user : " + user);
		//boolean result = gameroomRepository.joinGameroom(message.getGameroomId(), user.getUsername());
		
		Integer roomId = Integer.parseInt(message.getMessage());
		
		boolean result = gameroomRepository.joinGameroom(roomId, message.getWriter());
		
		user.setGameroomId(roomId);
		
		message.setMessageType(MessageType.ALERT);
		message.setMessage(message.getWriter() + "님이 참가하였습니다.");
		
		System.out.println("join message : " + message);
		smt.convertAndSend("/sub/gameroom/" + roomId, message);
	}
	
	@MessageMapping("/test")
	public void test(ChatMessage message, @AuthenticationPrincipal AuthUser user) {
		//System.out.println("test recv message : " + message);
	
		//System.out.println("test user user : " + user);
		//boolean result = gameroomRepository.joinGameroom(message.getGameroomId(), user.getUsername());
		
	}
	
	@MessageMapping("/quit")
	public void quitMessage(@AuthenticationPrincipal AuthUser user) {
		gameroomRepository.quitGameroom(user.getGameroomId(), user.getUsername());
		
		/*
		message.setType(MessageType.ALERT);
		message.setMessage(message.getWriter() + "님이 퇴장하였습니다.");
		System.out.println("qmessage : " + message);
		
		smt.convertAndSend("/sub/gameroom/" + message.getGameroomId(), message);
		*/
		ChatMessage message = ChatMessage
									.builder()
									.setType(MessageType.ALERT)
									.setWriter(user.getUsername())
									.build();
		
		smt.convertAndSend("/sub/gameroom/" + user.getGameroomId(), message);
		
		user.setGameroomId(null);
	}
	
	@MessageMapping("/step")
	public void sendGameStep(GameMessage message, @AuthenticationPrincipal AuthUser user) {
		Gameroom gameroom = gameroomRepository.getGameroom(message.getRoomId());
		gameroom.gameProcess(message);
		
		
		
		//
		/*
		 * gameroom = repo(message.getGameroomId());
		 * if gameMessage.getType() == START ?
		 * 	gameroom.start()
		 * else if (ASSIGN_WORDS) {
		 * 	
		 * }
		 *  
		 *  
		// */
		
	}

}
