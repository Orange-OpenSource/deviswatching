package com.orange.clara.tool.crawlers.github;

import com.orange.clara.tool.crawlers.github.services.CustomRepositoryService;
import com.orange.clara.tool.crawlers.github.services.Release;
import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.ResourceType;
import com.orange.clara.tool.model.WatchedResource;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.parser.Parser;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.UserService;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
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
public class GithubReleasesCrawlerTest extends AbstractGithubCrawlerTest {
    private static final String DESCRIPTION = "description";
    private static final String GITHUB_USER_REPO = "https://github.com/user/repo";
    private static final String NAME = "title";
    private static final String V0_0_2 = "v0.0.2";
    private static final String V0_0_1 = "v0.0.1";

    @InjectMocks
    GithubReleasesCrawler githubReleasesCrawler;
    @Mock
    CustomRepositoryService repositoryService;
    @Mock
    UserService userService;

    User user;

    List<Release> releases;

    @Before
    public void init() throws IOException {
        initMocks(this);
        user = this.generateUser();
        releases = this.generateReleases();
        githubReleasesCrawler.markdownParser = this.markdownParser();
        githubReleasesCrawler.markdownHtmlRenderer = this.markdownHtmlRenderer();
        when(repositoryService.getReleases(new RepositoryId("user", "repo"))).thenReturn(this.releases);
        when(userService.getUser("user")).thenReturn(this.user);
    }


    public void when_trying_to_get_new_resource_content_for_the_first_time_it_should_return_all_content_resources() throws Exception {
        WatchedResource watchedResource = new WatchedResource();
        watchedResource.setType(ResourceType.GITHUB_RELEASES);
        watchedResource.setLink(GITHUB_USER_REPO);
        List<ContentResource> contentResources = githubReleasesCrawler.getLastContent(watchedResource);
        assertThat(contentResources.size()).isEqualTo(2);
    }

    public void when_trying_to_get_new_resource_content_from_a_date_it_should_return_only_news_ones() throws Exception {
        Date date = Calendar.getInstance().getTime();
        LocalDateTime ldt = LocalDateTime.from(date.toInstant().atZone(ZoneId.systemDefault())).minusDays(1);
        ZonedDateTime zonedDateTime = ldt.atZone(ZoneId.systemDefault());
        Date dateMinus = Date.from(zonedDateTime.toInstant());

        WatchedResource watchedResource = new WatchedResource();
        watchedResource.setType(ResourceType.GITHUB_RELEASES);
        watchedResource.setUpdatedResourceAt(dateMinus);
        watchedResource.setLink(GITHUB_USER_REPO);
        List<ContentResource> contentResources = githubReleasesCrawler.getLastContent(watchedResource);
        assertThat(contentResources.size()).isEqualTo(1);
        assertThat(contentResources.get(0).getAuthor()).isEqualTo(V0_0_2);
    }

    public void when_asking_for_image_it_should_return_image_from_the_resource() throws Exception {
        WatchedResource watchedResource = new WatchedResource();
        watchedResource.setType(ResourceType.GITHUB_RELEASES);
        watchedResource.setLink(GITHUB_USER_REPO);
        assertThat(githubReleasesCrawler.getImage(watchedResource)).isEqualTo(AVATAR_URL);
    }

    private Parser markdownParser() {
        return Parser.builder().extensions(markdownExtension()).build();
    }

    private HtmlRenderer markdownHtmlRenderer() {
        return HtmlRenderer.builder().extensions(markdownExtension()).build();
    }

    private List<Extension> markdownExtension() {
        return Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create());
    }


    private List<Release> generateReleases() {
        Release release1 = new Release();
        release1.setBody(DESCRIPTION);
        Date date = Calendar.getInstance().getTime();
        release1.setCreatedAt(date);
        release1.setAuthor(this.user);
        release1.setTagName(V0_0_2);
        release1.setUrl(GITHUB_USER_REPO);
        release1.setName(NAME);


        Release release2 = new Release();
        release2.setBody(DESCRIPTION);

        LocalDateTime ldt = LocalDateTime.from(date.toInstant().atZone(ZoneId.systemDefault())).minusDays(2);
        ZonedDateTime zonedDateTime = ldt.atZone(ZoneId.systemDefault());
        Date dateMinus = Date.from(zonedDateTime.toInstant());
        release2.setCreatedAt(dateMinus);
        release2.setAuthor(this.user);
        release2.setTagName(V0_0_1);
        release2.setUrl(GITHUB_USER_REPO);
        release2.setName(NAME);
        return Arrays.asList(release1, release2);
    }
}