package com.orange.clara.tool.model.response;

import com.orange.clara.tool.model.EnumOauthProvider;

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
public class InfoResponse {

    private EnumOauthProvider oauthProviderType;
    private String webSocketEndpoint;

    public EnumOauthProvider getOauthProviderType() {
        return oauthProviderType;
    }

    public void setOauthProviderType(EnumOauthProvider oauthProviderType) {
        this.oauthProviderType = oauthProviderType;
    }

    public String getWebSocketEndpoint() {
        return webSocketEndpoint;
    }

    public void setWebSocketEndpoint(String webSocketEndpoint) {
        this.webSocketEndpoint = webSocketEndpoint;
    }

    @Override
    public String toString() {
        return "InfoResponse{" +
                "oauthProviderType=" + oauthProviderType +
                '}';
    }
}
