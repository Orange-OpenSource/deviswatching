package com.orange.clara.tool.schedulers;

import com.orange.clara.tool.exceptions.AsyncTaskException;
import com.orange.clara.tool.model.WatchedResource;
import com.orange.clara.tool.repos.WatchedResourceRepo;
import com.orange.clara.tool.schedulers.tasks.RefreshTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

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
@Component
public class SchedulerWatchedResources {

    @Autowired
    protected WatchedResourceRepo watchedResourceRepo;
    @Autowired
    protected RefreshTask refreshTask;
    private Logger logger = LoggerFactory.getLogger(SchedulerWatchedResources.class);
    @Value("${update.resource.after.minutes:10}")
    private Long updateAfterMinutes;

    @Scheduled(fixedDelay = 5000)
    public void refreshResource() throws AsyncTaskException {
        logger.debug("Running: refresh watched resource scheduled task ...");
        Iterable<WatchedResource> watchedResources = this.watchedResourceRepo.findAll();
        LocalDateTime whenRemoveDateTime;
        for (WatchedResource watchedResource : watchedResources) {
            if (watchedResource.getUpdatedResourceAt() == null) {
                continue;
            }
            whenRemoveDateTime = LocalDateTime.from(watchedResource.getUpdatedResourceAt().toInstant().atZone(ZoneId.systemDefault())).plusMinutes(updateAfterMinutes);
            if (LocalDateTime.from(Calendar.getInstance().toInstant().atZone(ZoneId.systemDefault())).isBefore(whenRemoveDateTime) || watchedResource.isLocked()) {
                continue;
            }
            watchedResource.setLocked(true);
            this.watchedResourceRepo.save(watchedResource);
            this.refreshTask.runTask(watchedResource.getId());
        }
        logger.debug("Finished: refresh watched resource scheduled task.");
    }
}
