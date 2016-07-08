package com.orange.clara.tool.crawlers.github;

import com.orange.clara.tool.crawlers.github.services.CustomRepositoryService;
import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.ResourceType;
import com.orange.clara.tool.model.WatchedResource;
import org.eclipse.egit.github.core.Repository;
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
 * Date: 27/06/2016
 */
public class GithubReposCrawlerTest extends AbstractGithubCrawlerTest {
    private static final String DESCRIPTION = "description";
    private static final String GITHUB_USER_REPO1 = "https://github.com/user/repo1";
    private static final String GITHUB_USER = "https://github.com/user";
    private static final String GITHUB_USER_REPO2 = "https://github.com/user/repo2";
    private static final String NAME = "title";

    @InjectMocks
    GithubReposCrawler githubReposCrawler;
    @Mock
    CustomRepositoryService repositoryService;
    @Mock
    UserService userService;

    private List<Repository> repositories;

    @Before
    public void init() throws IOException {
        initMocks(this);
        user = this.generateUser();
        this.repositories = this.generateRepositories();
        when(repositoryService.getRepositories("user")).thenReturn(this.repositories);
    }

    @Override
    public void when_trying_to_get_new_resource_content_for_the_first_time_it_should_return_all_content_resources() throws Exception {
        WatchedResource watchedResource = new WatchedResource();
        watchedResource.setType(ResourceType.GITHUB_REPOSITORIES);
        watchedResource.setLink(GITHUB_USER);
        List<ContentResource> contentResources = githubReposCrawler.getLastContent(watchedResource);
        assertThat(contentResources.size()).isEqualTo(2);
    }

    @Override
    public void when_trying_to_get_new_resource_content_from_a_date_it_should_return_only_news_ones() throws Exception {
        Date date = Calendar.getInstance().getTime();
        LocalDateTime ldt = LocalDateTime.from(date.toInstant().atZone(ZoneId.systemDefault())).minusDays(1);
        ZonedDateTime zonedDateTime = ldt.atZone(ZoneId.systemDefault());
        Date dateMinus = Date.from(zonedDateTime.toInstant());

        WatchedResource watchedResource = new WatchedResource();
        watchedResource.setType(ResourceType.GITHUB_REPOSITORIES);
        watchedResource.setLink(GITHUB_USER);
        watchedResource.setUpdatedResourceAt(dateMinus);

        List<ContentResource> contentResources = githubReposCrawler.getLastContent(watchedResource);
        assertThat(contentResources.size()).isEqualTo(1);
        assertThat(contentResources.get(0).getLink()).isEqualTo(GITHUB_USER_REPO1);
    }

    @Override
    public void when_asking_for_image_it_should_return_image_from_the_resource() throws Exception {
        //already tested in GithubReleasesCrawlerTest
    }

    private List<Repository> generateRepositories() {
        Repository repo1 = new Repository();
        repo1.setName(NAME);
        repo1.setUrl(GITHUB_USER_REPO1);
        repo1.setDescription(DESCRIPTION);
        repo1.setOwner(this.user);
        Date date = Calendar.getInstance().getTime();
        repo1.setCreatedAt(date);

        Repository repo2 = new Repository();
        repo2.setName(NAME);
        repo2.setUrl(GITHUB_USER_REPO2);
        repo2.setDescription(DESCRIPTION);
        repo2.setOwner(this.user);
        LocalDateTime ldt = LocalDateTime.from(date.toInstant().atZone(ZoneId.systemDefault())).minusDays(2);
        ZonedDateTime zonedDateTime = ldt.atZone(ZoneId.systemDefault());
        Date dateMinus = Date.from(zonedDateTime.toInstant());
        repo2.setCreatedAt(dateMinus);
        return Arrays.asList(repo1, repo2);
    }
}