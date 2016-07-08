package com.orange.clara.tool.notification.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.User;
import com.orange.clara.tool.model.WatchedResource;

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
public class NewContentEvent {
    @JsonIgnore
    private List<User> usersNotified;

    private WatchedResource watchedResource;
    private List<ContentResource> contentResourceList;

    public NewContentEvent(List<User> usersNotified, WatchedResource watchedResource, List<ContentResource> contentResourceList) {
        this.usersNotified = usersNotified;
        this.watchedResource = watchedResource;
        this.contentResourceList = contentResourceList;
    }

    public WatchedResource getWatchedResource() {
        return watchedResource;
    }

    public void setWatchedResource(WatchedResource watchedResource) {
        this.watchedResource = watchedResource;
    }

    public List<ContentResource> getContentResourceList() {
        return contentResourceList;
    }

    public void setContentResourceList(List<ContentResource> contentResourceList) {
        this.contentResourceList = contentResourceList;
    }

    public List<User> getUsersNotified() {
        return usersNotified;
    }

    public void setUsersNotified(List<User> usersNotified) {
        this.usersNotified = usersNotified;
    }

    @Override
    public String toString() {
        return "NewContentEvent{" +
                "watchedResource=" + watchedResource +
                ", contentResourceList=" + contentResourceList +
                '}';
    }
}
