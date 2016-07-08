package com.orange.clara.tool.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 24/06/2016
 */
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserManage {
    private static Logger LOGGER = LoggerFactory.getLogger(UserManage.class);

    @Autowired
    private OAuth2AccessToken oAuth2AccessToken;


    @RequestMapping({"/user", "/me"})
    @ResponseBody
    public Principal user(Principal principal) {
        return principal;
    }

    @RequestMapping({"/token"})
    @ResponseBody
    public String token() {
        return this.oAuth2AccessToken.getValue();
    }

}
