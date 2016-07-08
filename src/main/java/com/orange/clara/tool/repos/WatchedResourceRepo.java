package com.orange.clara.tool.repos;

import com.orange.clara.tool.model.ResourceType;
import com.orange.clara.tool.model.WatchedResource;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
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
@RepositoryRestResource(collectionResourceRel = "watchedResource", path = "watchedResource")
public interface WatchedResourceRepo extends
        CrudRepository<WatchedResource, Integer>, JpaSpecificationExecutor<WatchedResource> {
    List<WatchedResource> findByTypeIn(List<ResourceType> resourceTypes);
}
