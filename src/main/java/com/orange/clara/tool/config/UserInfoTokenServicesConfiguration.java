package com.orange.clara.tool.config;

import com.orange.clara.tool.service.SsoUserDetailsService;
import com.orange.clara.tool.websocket.WebSocketOauthHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 30/06/2016
 */
@Configuration
public class UserInfoTokenServicesConfiguration {
    @Autowired
    private ResourceServerProperties sso;

    @Autowired(required = false)
    @Qualifier("userInfoRestTemplate")
    private OAuth2RestOperations restTemplate;


    @Bean
    public UserInfoTokenServices userInfoTokenServices() {
        UserInfoTokenServices services = new SsoUserDetailsService(
                this.sso.getUserInfoUri(), this.sso.getClientId());
        services.setRestTemplate(this.restTemplate);
        services.setTokenType(this.sso.getTokenType());
        return services;
    }

    @Bean
    public WebSocketOauthHandler webSocketOauthHandler() {
        return new WebSocketOauthHandler();
    }
}
