package com.orange.clara.tool.boot.sequences;

import com.orange.clara.tool.exceptions.BootSequenceException;
import com.orange.clara.tool.model.Role;
import com.orange.clara.tool.model.RoleType;
import com.orange.clara.tool.repos.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
@Component
@Order(1)
public class BootInitData implements BootSequence {

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public void runSequence() throws BootSequenceException {
        for (RoleType roleType : RoleType.values()) {
            createRoleIfNotFound(roleType);
        }
    }

    @Transactional
    private Role createRoleIfNotFound(RoleType name) {
        Role role = roleRepo.findFirstByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepo.save(role);
        }
        return role;
    }

}
