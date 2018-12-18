package com.project.chat.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.chat.gameroom.ChatMessage;
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

	Logger logger = LoggerFactory.getLogger(getClass());

	// 게임방 조회
	@GetMapping("/games")
	public String games(ModelMap modelMap, @AuthenticationPrincipal AuthUser user) {
		logger.info("[/games](GET) AuthUser : {} ", user);

		List<Gameroom> list = new ArrayList<>(gameroomRepository.getGamerooms());
		logger.info("  GameroomList : {}", list);

		modelMap.put("list", list);

		return "games";
	}

	// 게임방 생성
	@PostMapping("/games")
	public String createGame(@AuthenticationPrincipal AuthUser user) {
		logger.info("[/games](POST) AuthUser : {}", user);
		Integer id = gameroomRepository.createGameroom();

		logger.info("  created id : {}", id);
		return "redirect:/gameroom/" + id.toString();
	}

	// 게임방 참가
	/*
	 * 게임방에 참가할 수 없을 경우 - 정원이 가득참 - 게임방이 존재하지 않음
	 * 
	 * 에 대한 예외처리 필요합니다.
	 */
	@GetMapping("/gameroom/{id}")
	public String gameroom(@PathVariable Integer id, @AuthenticationPrincipal AuthUser user, ModelMap modelMap) {
		logger.info("[/gameroom/{id}](GET) Id : {} AuthUser : {}", id, user);

		Gameroom gameroom = gameroomRepository.getGameroom(id);
		logger.info("  Gameroom : {}", gameroom);

		modelMap.put("gameroom", gameroom);
		modelMap.put("userId", user.getUsername());
		modelMap.put("roomid", id);

		return "gameroom";
	}

	@GetMapping("/words")
	@ResponseBody
	public Map<String, String> keyWord(@AuthenticationPrincipal AuthUser user) {
		String word = gameroomRepository.getWord(user.getGameroomId(), user.getUsername());
		logger.info("[/words](GET) AuthUser : {}, word : {}", user, word);

		return Collections.singletonMap("word", word);
	}

	// client에서 보낸 메시지를 처리한다.
	// config에서 setApplicationDestinationPrefixes를 통해 등록한 url/send 이런식으로 클라이언트에서
	// 보낸다.
	// 메시지 응답은 /sub/gameroom/{roomid}" 이런식으로 하면 된다.
	@MessageMapping("/message")
	public void sendMessage(ChatMessage message, @AuthenticationPrincipal AuthUser user) {
		logger.info("[/message](SOCKET) AuthUser : {}, ChatMessage : {}", user, message);

		smt.convertAndSend("/sub/gameroom/" + user.getGameroomId(), message);
	}

	@MessageMapping("/join")
	public void joinMessage(ChatMessage message, @AuthenticationPrincipal AuthUser user) {
		logger.info("[/join](SOCKET) AuthUser : {}, ChatMessage : {}", user, message);

		Integer roomId = Integer.parseInt(message.getMessage());
		String msg = gameroomRepository.joinGameroom(roomId, user.getUsername());

		user.setGameroomId(roomId);

		message.setMessageType(MessageType.JOIN);
		message.setWriter(user.getUsername());
		message.setMessage(msg);

		smt.convertAndSend("/sub/gameroom/" + roomId, message);
		logger.info("  send {}", message);
	}

	@MessageMapping("/quit")
	public void quitMessage(@AuthenticationPrincipal AuthUser user) {
		logger.info("[/quit](SOCKET) AuthUser : {}", user);
		String msg = gameroomRepository.quitGameroom(user.getGameroomId(), user.getUsername());

		ChatMessage message = ChatMessage.builder().setType(MessageType.QUIT).setWriter(user.getUsername())
				.setMessage(msg).build();

		smt.convertAndSend("/sub/gameroom/" + user.getGameroomId(), message);
		logger.info("  send {}", message);

		user.setGameroomId(null);
	}

	@MessageMapping("/play")
	public void sendGameStep(ChatMessage message, @AuthenticationPrincipal AuthUser user) {
		logger.info("[/play](SOCKET) AuthUser : {}, ChatMessage : {}", user, message);

		if(gameroomRepository.gameProcess(user.getGameroomId(), message)) {
			smt.convertAndSend("/sub/gameroom/" + user.getGameroomId(), message);
			logger.info("  send {}", message);
		}
	}

}
