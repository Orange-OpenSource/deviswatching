package com.orange.clara.tool.controllers.websocket;

import com.orange.clara.tool.model.User;
import com.orange.clara.tool.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 01/07/2016
 */
@Controller
public class NotificationAckController {

    @Autowired
    private UserRepo userRepo;

    @MessageMapping("/notification.ack")
    public void notificationAcknowledge(Principal principal) {
        User user = this.userRepo.findOne(principal.getName());
        user.updateLastWatch();
        this.userRepo.save(user);
    }
}
