package com.orange.clara.tool.crawlers.rss;

import com.orange.clara.tool.crawlers.AbstractCrawlersTest;
import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.WatchedResource;
import com.orange.clara.tool.service.RssService;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Copyright (C) 2016 Arthur Halet
 * <p>
 * This software is distributed under the terms and conditions of the 'MIT'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'http://opensource.org/licenses/MIT'.
 * <p>
 * Author: Arthur Halet
 * Date: 25/06/2016
 */
public class RssCrawlerTest extends AbstractCrawlersTest {
    private static final String UPDATE_DATE = "26/06/2016";
    private static final String TITLE_1 = "Actor Johnny Depp's Basquiat art to go under the hammer";
    private static final String TITLE_2 = "David Hockney returns to London's Royal Academy with new art show";
    private static final String TITLE_3 = "Banksy's spray painted SWAT Van seen fetching $400,000 at auction";
    private static final String EXPECTED_IMAGE = "http://www.reuters.com/resources_v2/images/reuters125.png";
    @InjectMocks
    private RssCrawler rssCrawler;

    @Mock
    private RssService rssService;

    @Before
    public void init() throws IOException, FeedException {
        initMocks(this);
        when(rssService.getFeed((WatchedResource) notNull())).thenReturn(this.getRssResource());
    }

    @Override
    public void when_trying_to_get_new_resource_content_for_the_first_time_it_should_return_all_content_resources() throws Exception {
        WatchedResource watchedResource = new WatchedResource();
        watchedResource.setLink("anylink");
        List<ContentResource> contentResources = rssCrawler.getLastContent(watchedResource);
        assertThat(contentResources.size()).isEqualTo(6);
    }

    @Override
    public void when_trying_to_get_new_resource_content_from_a_date_it_should_return_only_news_ones() throws Exception {
        WatchedResource watchedResource = new WatchedResource();
        watchedResource.setLink("anylink");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = UPDATE_DATE;
        Date date = null;
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
        }
        watchedResource.setUpdatedResourceAt(date);
        List<ContentResource> contentResources = rssCrawler.getLastContent(watchedResource);
        Collections.sort(contentResources, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        assertThat(contentResources.size()).isEqualTo(3);
        assertThat(contentResources.get(0).getTitle()).isEqualTo(TITLE_1);
        assertThat(contentResources.get(1).getTitle()).isEqualTo(TITLE_2);
        assertThat(contentResources.get(2).getTitle()).isEqualTo(TITLE_3);
    }

    @Override
    public void when_asking_for_image_it_should_return_image_from_the_resource() throws Exception {
        WatchedResource watchedResource = new WatchedResource();
        watchedResource.setLink("anylink");
        assertThat(rssCrawler.getImage(watchedResource)).isEqualTo(EXPECTED_IMAGE);
    }

    private SyndFeed getRssResource() throws IOException, FeedException {
        URL url = this.getClass().getResource("/rss/artsculture.rss");
        SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(url));
    }
}