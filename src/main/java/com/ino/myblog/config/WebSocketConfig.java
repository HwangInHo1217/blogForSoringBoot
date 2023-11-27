/*
package com.ino.myblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.nio.file.AccessDeniedException;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authToken = accessor.getFirstNativeHeader("Authorization");
                    if (authToken != null && authToken.startsWith("Bearer ")) {
                        // 토큰에서 "Bearer " 접두어 제거
                        String jwtToken = authToken.substring(7);
                        try {
                            // JWT 토큰을 사용하여 인증 시도
                            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(jwtToken, null);
                            Authentication authentication = authenticationManager.authenticate(authRequest);

                            // 인증 객체를 SecurityContext에 저장
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        } catch (AuthenticationException e) {
                            // 인증 실패 시 처리 (예: 로그 기록)
                            SecurityContextHolder.clearContext();
                            try {
                                throw new AccessDeniedException("인증 실패: " + e.getMessage());
                            } catch (AccessDeniedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }
                return message;
            }
        });
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    public void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        // WebSocket 연결 시 Spring Security 설정 적용
        messages.simpMessageDestMatchers("/ws/**").authenticated();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS().
                setInterceptors(new HttpHandshakeInterceptor());
    }
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}*/
