package com.orange.clara.tool.repos;

import com.orange.clara.tool.model.Role;
import com.orange.clara.tool.model.RoleType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 30/06/2016
 */
@RepositoryRestResource(collectionResourceRel = "role", path = "role")
public interface RoleRepo extends
        PagingAndSortingRepository<Role, Integer> {
    Role findFirstByName(RoleType name);
}
