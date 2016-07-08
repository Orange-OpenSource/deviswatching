package com.orange.clara.tool.notification.listeners;

import com.orange.clara.tool.model.User;
import com.orange.clara.tool.notification.events.NewContentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

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
@Service
public class WebSocketListenerService {
    private Logger logger = LoggerFactory.getLogger(WebSocketListenerService.class);
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Async
    @EventListener
    public void sendNewContentEvent(NewContentEvent newContentEvent) {
        List<User> users = newContentEvent.getUsersNotified();
        for (User user : users) {
            logger.info("Send notification to {}", user.getName());
            this.messagingTemplate.convertAndSendToUser(user.getUuid(), "/notifier/newcontent", newContentEvent);
        }

    }
}
