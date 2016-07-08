package com.orange.clara.tool.schedulers.tasks;

import com.google.common.collect.Lists;
import com.orange.clara.tool.crawlers.Crawler;
import com.orange.clara.tool.crawlers.CrawlerFactory;
import com.orange.clara.tool.exceptions.AsyncTaskException;
import com.orange.clara.tool.exceptions.CrawlerGetContentException;
import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.User;
import com.orange.clara.tool.model.WatchedResource;
import com.orange.clara.tool.notification.NotificationPublisher;
import com.orange.clara.tool.repos.ContentResourceRepo;
import com.orange.clara.tool.repos.WatchedResourceRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 29/06/2016
 */
public class RefreshTask {
    @Autowired
    protected WatchedResourceRepo watchedResourceRepo;
    @Autowired
    protected ContentResourceRepo contentResourceRepo;
    @Autowired
    protected CrawlerFactory crawlerFactory;
    private Logger logger = LoggerFactory.getLogger(RefreshTask.class);
    @Autowired
    private NotificationPublisher notificationPublisher;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Future<Boolean> runTask(Integer watchedResourceId) throws AsyncTaskException {
        WatchedResource watchedResource = this.watchedResourceRepo.findOne(watchedResourceId);
        logger.debug("Task: refresh {} from {} with type {} ...", watchedResourceId, watchedResource.getLink(), watchedResource.getType());
        Crawler crawler = crawlerFactory.findCrawler(watchedResource.getType());
        List<ContentResource> contentResources = null;
        try {
            contentResources = crawler.getLastContent(watchedResource);
        } catch (CrawlerGetContentException e) {
            throw new AsyncTaskException(e.getMessage(), e);
        }
        for (ContentResource contentResource : contentResources) {
            watchedResource.addContentResource(contentResource);
            this.contentResourceRepo.save(contentResource);
        }
        watchedResource.setUpdatedResourceAt(Calendar.getInstance().getTime());
        watchedResource.setLocked(false);
        this.watchedResourceRepo.save(watchedResource);
        if (contentResources.size() > 0) {
            this.publishNotification(watchedResource, contentResources);
        }
        logger.debug("Finished Task: refresh {} from {} with type {}. {} new content(s) found.", watchedResourceId, watchedResource.getLink(), watchedResource.getType(), contentResources.size());
        return new AsyncResult<Boolean>(true);
    }

    private void publishNotification(WatchedResource watchedResource, List<ContentResource> contentResources) {
        WatchedResource partialWatchedResource = watchedResource.generatePartial();
        List<ContentResource> partialContentResources = Lists.newArrayList();
        for (ContentResource contentResource : contentResources) {
            partialContentResources.add(contentResource.generatePartial());
        }
        List<User> userToBeNotified = Lists.newArrayList();
        for (User user : watchedResource.getUsers()) {
            userToBeNotified.add(user.generatePartial());
        }
        this.notificationPublisher.publishNewContentEvent(userToBeNotified, partialWatchedResource, partialContentResources);
    }

}
