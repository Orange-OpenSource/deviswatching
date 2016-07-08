package com.orange.clara.tool.crawlers.rss;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.orange.clara.tool.crawlers.Crawler;
import com.orange.clara.tool.exceptions.CrawlerGetContentException;
import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.ResourceType;
import com.orange.clara.tool.model.WatchedResource;
import com.orange.clara.tool.service.RssService;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 25/06/2016
 */
@Component
public class RssCrawler implements Crawler {

    @Autowired
    protected RssService rssService;

    @Override
    public List<ContentResource> getLastContent(WatchedResource watchedResource) throws CrawlerGetContentException {
        try {
            SyndFeed feed = this.rssService.getFeed(watchedResource);
            List<ContentResource> contentResources = Lists.newArrayList();
            ContentResource contentResource;
            for (SyndEntry entry : feed.getEntries()) {
                contentResource = this.extractContent(entry, feed);
                if (watchedResource.getUpdatedResourceAt() != null &&
                        contentResource.getDate().before(watchedResource.getUpdatedResourceAt())) {
                    continue;
                }
                contentResources.add(this.extractContent(entry, feed));
            }
            return contentResources;
        } catch (Exception e) {
            throw new CrawlerGetContentException(e.getMessage(), e);
        }
    }

    @Override
    public String getImage(WatchedResource watchedResource) throws CrawlerGetContentException {
        try {
            SyndFeed feed = this.rssService.getFeed(watchedResource);
            return feed.getImage().getUrl();
        } catch (IOException | FeedException e) {
            throw new CrawlerGetContentException(e.getMessage(), e);
        }
    }

    @Override
    public String generateTitle(WatchedResource watchedResource) throws CrawlerGetContentException {
        URI uri = URI.create(watchedResource.getLink());
        return "Rss feed from " + uri.getHost() + uri.getPath();
    }

    @Override
    public ResourceType forResourceType() {
        return ResourceType.RSS;
    }


    private ContentResource extractContent(SyndEntry entry, SyndFeed feed) throws IOException {
        ContentResource contentResource = new ContentResource();
        contentResource.setTitle(entry.getTitle());
        if (!entry.getEnclosures().isEmpty()) {
            contentResource.setImage(entry.getEnclosures().get(0).getUrl());
        }
        if (entry.getContents().isEmpty()) {
            contentResource.setDescription(entry.getDescription().getValue());
        } else {
            contentResource.setDescription(Joiner.on("\n").join(entry.getContents()));
        }
        if (entry.getAuthor() == null || entry.getAuthor().isEmpty()) {
            contentResource.setAuthor(feed.getTitle());
        } else {
            contentResource.setAuthor(entry.getAuthor());
        }
        contentResource.setDate(entry.getPublishedDate());
        contentResource.setLink(entry.getLink());
        return contentResource;
    }
}
