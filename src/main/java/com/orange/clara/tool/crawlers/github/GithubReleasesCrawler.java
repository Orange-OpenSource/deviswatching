package com.orange.clara.tool.crawlers.github;

import com.google.common.collect.Lists;
import com.orange.clara.tool.crawlers.Crawler;
import com.orange.clara.tool.crawlers.github.services.Release;
import com.orange.clara.tool.exceptions.CrawlerGetContentException;
import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.ResourceType;
import com.orange.clara.tool.model.WatchedResource;
import org.eclipse.egit.github.core.RepositoryId;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
@Component
public class GithubReleasesCrawler extends AbstractGithubCrawler implements Crawler {

    @Override
    public List<ContentResource> getLastContent(WatchedResource watchedResource) throws CrawlerGetContentException {
        List<Release> releases = this.getReleases(watchedResource);
        List<ContentResource> contentResources = Lists.newArrayList();
        ContentResource contentResource;
        for (Release release : releases) {
            try {
                contentResource = this.extractContent(release);

            } catch (IOException e) {
                throw new CrawlerGetContentException(e.getMessage(), e);
            }
            if (watchedResource.getUpdatedResourceAt() != null &&
                    contentResource.getDate().before(watchedResource.getUpdatedResourceAt())) {
                continue;
            }
            contentResources.add(contentResource);
        }

        return contentResources;
    }

    @Override
    public String generateTitle(WatchedResource watchedResource) throws CrawlerGetContentException {
        RepositoryId repositoryId = getRepositoryIdFromRepoName(watchedResource);
        return "Github releases in repository " + repositoryId.getOwner() + "/" + repositoryId.getName();
    }


    @Override
    public ResourceType forResourceType() {
        return ResourceType.GITHUB_RELEASES;
    }


}
