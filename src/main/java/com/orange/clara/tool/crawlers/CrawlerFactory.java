package com.orange.clara.tool.crawlers;

import com.google.common.collect.Maps;
import com.orange.clara.tool.model.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

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
public class CrawlerFactory {
    private Map<ResourceType, Crawler> crawlerMap;

    @Autowired
    private List<Crawler> crawlers;

    public Crawler findCrawler(ResourceType resourceType) {
        return crawlerMap.get(resourceType);
    }

    @PostConstruct
    public void init() {
        crawlerMap = Maps.newHashMap();
        for (Crawler crawler : crawlers) {
            crawlerMap.put(crawler.forResourceType(), crawler);
        }

    }
}
