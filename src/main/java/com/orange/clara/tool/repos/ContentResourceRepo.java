package com.orange.clara.tool.repos;

import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.WatchedResource;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

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
@RepositoryRestResource(collectionResourceRel = "contentResource", path = "contentResource")
public interface ContentResourceRepo extends
        PagingAndSortingRepository<ContentResource, Integer> {
    List<ContentResource> findAllByOrderByDateDesc();

    List<ContentResource> findByWatchedResourceOrderByDateDesc(WatchedResource watchedResource);
}
