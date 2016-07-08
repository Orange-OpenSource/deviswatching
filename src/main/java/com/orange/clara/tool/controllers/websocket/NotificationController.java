package com.orange.clara.tool.controllers.websocket;

import com.orange.clara.tool.notification.events.NewContentEvent;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

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
public class NotificationController {

    @SendTo("/notifier/newcontent")
    public NewContentEvent broadcastNewContentEvent(NewContentEvent newContentEvent) {
        return newContentEvent;
    }
}
