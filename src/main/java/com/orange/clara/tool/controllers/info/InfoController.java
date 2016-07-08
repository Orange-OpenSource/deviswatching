package com.orange.clara.tool.controllers.info;

import com.orange.clara.tool.config.WebSocketConfig;
import com.orange.clara.tool.model.EnumOauthProvider;
import com.orange.clara.tool.model.response.InfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 04/07/2016
 */
@RestController
@RequestMapping("/info")
public class InfoController {

    @Autowired
    @Qualifier("oauthProvider")
    private EnumOauthProvider oauthProvider;

    @Autowired
    @Qualifier("appUri")
    private String appUri;

    @RequestMapping(method = RequestMethod.GET, value = "/app")
    public InfoResponse getInformation() {
        InfoResponse infoResponse = new InfoResponse();
        infoResponse.setOauthProviderType(oauthProvider);
        infoResponse.setWebSocketEndpoint(appUri + WebSocketConfig.WEB_SOCKET_ENDPOINT);
        return infoResponse;
    }
}
