package com.orange.clara.tool.notification;

import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.User;
import com.orange.clara.tool.model.WatchedResource;
import com.orange.clara.tool.notification.events.NewContentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

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
@Component
public class NotificationPublisher {
    @Autowired
    private ApplicationEventPublisher publisher;

    public void publishNewContentEvent(List<User> usersToBeNotified, WatchedResource watchedResource, List<ContentResource> contentResources) {
        this.publisher.publishEvent(new NewContentEvent(usersToBeNotified, watchedResource, contentResources));
    }

}
