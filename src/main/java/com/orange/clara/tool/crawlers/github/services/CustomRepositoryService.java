package com.orange.clara.tool.crawlers.github.services;

import com.google.gson.reflect.TypeToken;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PagedRequest;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.List;

import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_REPOS;

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
public class CustomRepositoryService extends RepositoryService {
    public CustomRepositoryService() {
    }

    public CustomRepositoryService(GitHubClient client) {
        super(client);
    }

    public List<Release> getReleases(final IRepositoryIdProvider provider)
            throws IOException {
        final String id = getId(provider);
        PagedRequest<Release> request = createPagedRequest();
        request.setUri(SEGMENT_REPOS + '/' + id + "/releases");
        request.setType(new TypeToken<List<Release>>() {
        }.getType());
        return getAll(request);
    }

    public List<Release> getReleases(final String owner, final String name)
            throws IOException {
        return getReleases(RepositoryId.create(owner, name));
    }
}
