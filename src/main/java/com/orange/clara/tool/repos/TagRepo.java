package com.orange.clara.tool.repos;

import com.orange.clara.tool.model.Tag;
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
 * Date: 25/06/2016
 */
@RepositoryRestResource(exported = false)
public interface TagRepo extends
        PagingAndSortingRepository<Tag, Integer> {
    Tag findFirstByName(String name);

    List<Tag> findByNameContaining(String tagToFind);

    Tag findFirstByNameContaining(String tagToFind);


}
