package com.orange.clara.tool.crawlers;

import com.orange.clara.tool.exceptions.CrawlerGetContentException;
import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.ResourceType;
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
 * Date: 24/06/2016
 */
public interface Crawler {

    List<ContentResource> getLastContent(WatchedResource watchedResource) throws CrawlerGetContentException;

    String getImage(WatchedResource watchedResource) throws CrawlerGetContentException;

    String generateTitle(WatchedResource watchedResource) throws CrawlerGetContentException;

    ResourceType forResourceType();
}
