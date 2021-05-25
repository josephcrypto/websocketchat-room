package cn.us.coding.controller;

import cn.us.coding.model.ChatMessage;
import cn.us.coding.model.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations sendingOperations;

    @EventListener
    public void handleWebSocketConnectionListener(final SessionConnectedEvent event){
        LOGGER.info("Yo! We got a new Connection :)");
    }
    @EventListener
    public void handleWebSocketConnectionListener(final SessionDisconnectEvent event){

        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        final String username = (String) headerAccessor.getSessionAttributes().get("username");

        final ChatMessage chatMessage = ChatMessage.builder().type(MessageType.DISCONNECT)
                .sender(username).build();
        sendingOperations.convertAndSend("/topic/public",chatMessage);

    }
}
