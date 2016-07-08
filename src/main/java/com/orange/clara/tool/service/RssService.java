package com.orange.clara.tool.service;

import com.google.common.collect.Lists;
import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.ResourceType;
import com.orange.clara.tool.model.WatchedResource;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
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
@Service
public class RssService {
    public static final String FEED_TYPE = "rss_2.0";
    @Autowired
    @Qualifier("appUrl")
    public String appUrl;
    @Autowired
    @Qualifier("appName")
    public String appName;

    public SyndFeed getFeed(WatchedResource watchedResource) throws IOException, FeedException {
        return this.getFeed(watchedResource.getLink());
    }

    public SyndFeed getFeed(String url) throws IOException, FeedException {
        URL feedUrl = new URL(url);
        return this.getFeed(feedUrl);
    }

    public SyndFeed getFeed(URL url) throws IOException, FeedException {
        SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(url));
    }

    public SyndEntry convertContentResource(ContentResource contentResource) {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle(contentResource.getTitle());
        entry.setLink(contentResource.getLink());
        entry.setPublishedDate(contentResource.getDate());
        entry.setAuthor(contentResource.getAuthor());
        entry.setComments(contentResource.getWatchedResource().getType().toString());
        SyndContent description = new SyndContentImpl();
        description.setType("text/html");
        description.setValue(contentResource.getDescription());
        entry.setDescription(description);
        if (contentResource.getImage() != null) {
            SyndEnclosure syndEnclosure = new SyndEnclosureImpl();
            syndEnclosure.setUrl(contentResource.getImage());
            entry.setEnclosures(Arrays.asList(syndEnclosure));
        }
        return entry;
    }

    public List<SyndEntry> getEntriesFromWatchedResource(WatchedResource watchedResource) {
        List<SyndEntry> syndEntries = Lists.newArrayList();
        for (ContentResource contentResource : watchedResource.getContentResources()) {
            syndEntries.add(this.convertContentResource(contentResource));
        }
        return syndEntries;
    }

    public List<SyndEntry> getEntriesFromWatchedResources(List<WatchedResource> watchedResources) {
        List<SyndEntry> syndEntries = Lists.newArrayList();
        for (WatchedResource watchedResource : watchedResources) {
            syndEntries.addAll(this.getEntriesFromWatchedResource(watchedResource));
        }
        Collections.sort(syndEntries, (o1, o2) -> o2.getPublishedDate().compareTo(o1.getPublishedDate()));
        return syndEntries;
    }

    public SyndFeed generateFeed(WatchedResource watchedResource) {
        SyndFeed feed = this.generateDefaultFeed();
        feed.setTitle(watchedResource.getTitle());
        feed.setDescription("Generated feed for " + watchedResource.getLink());
        if (watchedResource.getImage() != null) {
            feed.setImage(this.generateImage(watchedResource));
        }

        feed.setEntries(this.getEntriesFromWatchedResource(watchedResource));
        return feed;
    }

    public SyndFeed generateFeed(List<WatchedResource> watchedResources) {
        SyndFeed feed = this.generateDefaultFeed();
        feed.setEntries(this.getEntriesFromWatchedResources(watchedResources));
        return feed;
    }

    public String getOutput(SyndFeed feed) throws FeedException {
        SyndFeedOutput output = new SyndFeedOutput();
        return output.outputString(feed);
    }

    private SyndFeed generateDefaultFeed() {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType(RssService.FEED_TYPE);
        feed.setLink(appUrl);
        feed.setAuthor(getAppName());
        feed.setTitle(getAppName() + " feed");
        return feed;
    }

    public SyndImage generateImage(String imageUrl) {
        SyndImage image = new SyndImageImpl();
        URI imageUri = URI.create(imageUrl);
        File imageFile = new File(imageUri.getPath());
        image.setTitle(imageFile.getName() + " image");
        image.setUrl(imageUrl);
        image.setLink(imageUrl);
        return image;
    }

    public SyndImage generateImage(WatchedResource watchedResource) {
        SyndImage image = this.generateImage(watchedResource.getImage());
        image.setTitle(watchedResource.getTitle() + " image");
        return image;
    }

    public SyndImage generateImage(ResourceType resourceType) {
        SyndImage image = this.generateImage(resourceType.getDefaultImageUrl());
        image.setTitle(resourceType.name().toLowerCase().replace("_", " ") + " type image");
        return image;
    }

    private String getAppName() {
        return appName.substring(0, 1).toUpperCase() + appName.substring(1).toLowerCase();
    }
}
