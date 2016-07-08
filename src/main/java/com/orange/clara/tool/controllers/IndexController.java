package com.orange.clara.tool.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URL;

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
@Controller
public class IndexController {

    @Autowired
    @Qualifier("uiUrl")
    private URL uiUrl;

    @Autowired
    private OAuth2AccessToken oAuth2AccessToken;

    @RequestMapping("/")
    public String index() {
        if (uiUrl == null) {
            return "no-ui";
        }
        return "redirect:" + uiUrl.toString() + "/#/?token=" + this.oAuth2AccessToken.getValue();
    }
}
