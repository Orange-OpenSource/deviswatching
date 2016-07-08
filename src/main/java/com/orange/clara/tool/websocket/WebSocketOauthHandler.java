package com.orange.clara.tool.websocket;

import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

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
public class WebSocketOauthHandler extends DefaultHandshakeHandler {

    private ResourceServerTokenServices resourceServerTokenServices;


    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String query = request.getURI().toString().split("\\?")[1];
        final Map<String, String> params = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(query);

        String token = "";
        if (params.get("token") != null) {
            token = params.get("token");
        }
        return (Principal) this.resourceServerTokenServices.loadAuthentication(token).getPrincipal();
    }

    @Autowired
    public void setResourceServerTokenServices(ResourceServerTokenServices resourceServerTokenServices) {
        this.resourceServerTokenServices = resourceServerTokenServices;
    }
}
