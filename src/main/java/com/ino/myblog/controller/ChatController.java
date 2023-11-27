/*
package com.ino.myblog.controller;

import com.ino.myblog.dto.ChatMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ino.myblog.config.auth.PrincipalDetail;
import com.ino.myblog.model.ChatMessage;
import com.ino.myblog.model.User;
import com.ino.myblog.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/send")
    @SendTo("/topic/public")
    public void sendChatMessage(@Payload ChatMessageDto chatMessageDto,
                                @AuthenticationPrincipal PrincipalDetail principalDetail) {
        System.out.println("principalDetail = " + principalDetail.getUser());
        if (principalDetail != null) {
            User user = principalDetail.getUser();
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUser(user);
            chatMessage.setContent(chatMessageDto.getContent());
            chatMessage.setTimestamp(LocalDateTime.now());
            chatService.saveChatMessage(chatMessage);
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        } else {
            logger.error("PrincipalDetail is null, indicating the user is not logged in.");
            // Error handling logic here
        }
    }

}*/
