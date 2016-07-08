package com.orange.clara.tool.crawlers.github;

import com.orange.clara.tool.crawlers.Crawler;
import com.orange.clara.tool.crawlers.github.services.CustomRepositoryService;
import com.orange.clara.tool.crawlers.github.services.Release;
import com.orange.clara.tool.exceptions.CrawlerGetContentException;
import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.WatchedResource;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 27/06/2016
 */
public abstract class AbstractGithubCrawler implements Crawler {
    protected final static String REPO_LINK = "https://github.com/([^/]+)/([^/]+)";
    protected final static String ORG_USER_LINK = "https://github.com/([^/]+)";

    @Autowired
    @Qualifier("markdownParser")
    protected Parser markdownParser;

    @Autowired
    @Qualifier("markdownHtmlRenderer")
    protected HtmlRenderer markdownHtmlRenderer;

    @Autowired
    @Qualifier("customRepositoryService")
    protected CustomRepositoryService repositoryService;

    @Autowired
    @Qualifier("userService")
    protected UserService userService;

    protected RepositoryId getRepositoryId(WatchedResource watchedResource) throws CrawlerGetContentException {
        switch (watchedResource.getType()) {
            case GITHUB_RELEASES:
                return this.getRepositoryIdFromRepoName(watchedResource);
            default:
                return this.getRepositoryIdFromOwner(watchedResource);
        }
    }

    protected RepositoryId getRepositoryIdFromRepoName(WatchedResource watchedResource) throws CrawlerGetContentException {
        String link = watchedResource.getLink();
        Pattern p = Pattern.compile(REPO_LINK);
        Matcher m = p.matcher(link);
        if (!m.find()) {
            throw new CrawlerGetContentException("Given link is not a valid github repo link: '" + link + "'");
        }
        String owner = m.group(1);
        String repoName = m.group(2);
        return new RepositoryId(owner, repoName);
    }

    protected RepositoryId getRepositoryIdFromOwner(WatchedResource watchedResource) throws CrawlerGetContentException {
        String link = watchedResource.getLink();
        Pattern p = Pattern.compile(ORG_USER_LINK);
        Matcher m = p.matcher(link);
        if (!m.find()) {
            throw new CrawlerGetContentException("Given link is not a owner/org github link: '" + link + "'");
        }
        String owner = m.group(1);
        return new RepositoryId(owner, "noname");
    }

    protected List<Repository> getRepositories(WatchedResource watchedResource) throws CrawlerGetContentException {
        String link = watchedResource.getLink();
        Pattern p = Pattern.compile(ORG_USER_LINK);
        Matcher m = p.matcher(link);
        if (!m.find()) {
            throw new CrawlerGetContentException("Given link is not a owner/org github link: '" + link + "'");
        }
        String owner = m.group(1);
        try {
            return repositoryService.getRepositories(owner);
        } catch (IOException e) {
            throw new CrawlerGetContentException(e.getMessage(), e);
        }
    }

    protected List<Release> getReleases(WatchedResource watchedResource) throws CrawlerGetContentException {
        RepositoryId repositoryId = this.getRepositoryId(watchedResource);
        try {
            return repositoryService.getReleases(repositoryId);
        } catch (IOException e) {
            throw new CrawlerGetContentException(e.getMessage(), e);
        }
    }

    protected ContentResource extractContent(Release release) throws IOException {
        ContentResource contentResource = new ContentResource();
        contentResource.setTitle(release.getName());
        contentResource.setDescription(markdownParse(release.getBody()));
        contentResource.setAuthor(release.getTagName());
        contentResource.setDate(release.getCreatedAt());
        contentResource.setLink(release.getUrl());
        return contentResource;
    }

    protected ContentResource extractContent(Repository repository) throws IOException {
        ContentResource contentResource = new ContentResource();
        contentResource.setTitle(repository.getName());
        contentResource.setDescription(repository.getDescription());
        contentResource.setAuthor(repository.getOwner().getLogin());
        contentResource.setDate(repository.getCreatedAt());
        contentResource.setLink(repository.getHtmlUrl());
        return contentResource;
    }

    @Override
    public String getImage(WatchedResource watchedResource) throws CrawlerGetContentException {
        RepositoryId repositoryId = this.getRepositoryId(watchedResource);
        try {
            User user = userService.getUser(repositoryId.getOwner());
            return user.getAvatarUrl();
        } catch (IOException e) {
            throw new CrawlerGetContentException(e.getMessage(), e);
        }
    }

    public String markdownParse(String source) {
        Node document = markdownParser.parse(source);
        return markdownHtmlRenderer.render(document);
    }
}
