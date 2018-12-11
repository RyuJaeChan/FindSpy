package com.project.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//해당 endpoint로 handshake가 이루어진다.
		registry.addEndpoint("/sock").setAllowedOrigins("*").withSockJS();
        //registry.addEndpoint("/chat").withSockJS();
	}

	@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
		//client에서 SEND 요청을 처리한다.
		config.setApplicationDestinationPrefixes("/game");
		
		/*
		Spring Refernece에서는 /topic, /queue가 주로 등장한다.
		tobpic : 암시적으로 1:N 전파를 의미
		queue  : 암시적으로 1:1 전파를 의미
		enableSImpleBroker : 해당 경로로 SimpleBroker를 등록한다.
		SimpleBroker는 해당 경로를 SUBSCRIBE하는 client에게 메시지를 전달하는 작업을 수행한다.
		*/
        config.enableSimpleBroker("/sub");
    }
 

}
