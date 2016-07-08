package com.orange.clara.tool.controllers;

import com.orange.clara.tool.model.ResourceType;
import com.orange.clara.tool.model.User;
import com.orange.clara.tool.model.WatchedResource;
import com.orange.clara.tool.service.RssService;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndImage;
import com.rometools.rome.io.FeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
@Controller
@RequestMapping("/rss")
public class RssController extends AbstractDefaultController {


    @Autowired
    private RssService rssService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public
    @ResponseBody
    String getFeed(@RequestParam(value = "tags", required = false) String tags,
                   @RequestParam(value = "types", required = false) String types,
                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(value = "after_date", required = false) Date afterDate
    ) throws FeedException {
        User user = this.getCurrentUser();

        List<WatchedResource> watchedResources = this.getFilteredWatchedResources(user, false, null, tags, types, afterDate);
        SyndFeed feed = rssService.generateFeed(watchedResources);
        if (types != null && tags == null) {
            feed.setImage(this.getImageFromListTypes(types));
        }
        feed.setTitle(feed.getTitle() + this.generateTitle(tags, types));
        feed.setDescription(this.generateDescription(tags, types));
        return rssService.getOutput(feed);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}*")
    public
    @ResponseBody
    ResponseEntity<String> getFeed(@PathVariable("id") Integer id) throws FeedException {
        User user = this.getCurrentUser();
        WatchedResource watchedResource = this.watchedResourceRepo.findOne(id);
        if (watchedResource == null) {
            return generateEntityFromStatus(HttpStatus.NOT_FOUND);
        }
        if (!watchedResource.hasUser(user) && !this.isCurrentUserAdmin()) {
            return generateEntityFromStatus(HttpStatus.UNAUTHORIZED);
        }
        SyndFeed feed = rssService.generateFeed(watchedResource);
        return ResponseEntity.ok(rssService.getOutput(feed));
    }

    private SyndImage getImageFromListTypes(String types) {
        List<ResourceType> resourceTypes = ResourceType.fromStringList(types);
        if (resourceTypes.size() == 0) {
            return null;
        }
        return this.rssService.generateImage(resourceTypes.get(0));
    }

    private String generateTitle(String tags, String types) {
        if (tags == null && types == null) {
            return " (All Resources)";
        }
        String title = "";
        if (tags != null) {
            title += " with tag(s) " + tags;
        }
        if (types != null) {
            title += " with type(s) " + types;
        }
        return title;
    }

    private String generateDescription(String tags, String types) {
        if (tags == null && types == null) {
            return "Generated feed for all watched resources";
        }
        return "Generated feed for watched resources" + this.generateTitle(tags, types);
    }

}
