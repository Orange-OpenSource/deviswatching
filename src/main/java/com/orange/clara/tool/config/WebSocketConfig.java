package com.orange.clara.tool.config;

import com.orange.clara.tool.websocket.WebSocketOauthHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 08/07/2016
 */
@Configuration
@EnableWebSocketMessageBroker
@ComponentScan(basePackages = "com.orange.clara.tool")
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    private WebSocketOauthHandler webSocketOauthHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/notifier");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .setHandshakeHandler(this.webSocketOauthHandler)
                .withSockJS();
    }

    @Autowired
    public void setWebSocketOauthHandler(WebSocketOauthHandler webSocketOauthHandler) {
        this.webSocketOauthHandler = webSocketOauthHandler;
    }
}
