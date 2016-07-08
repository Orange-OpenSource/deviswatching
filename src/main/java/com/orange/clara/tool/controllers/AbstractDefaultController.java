package com.orange.clara.tool.controllers;

import com.google.common.collect.Lists;
import com.orange.clara.tool.model.*;
import com.orange.clara.tool.repos.TagRepo;
import com.orange.clara.tool.repos.UserRepo;
import com.orange.clara.tool.repos.WatchedResourceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.criteria.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 01/07/2016
 */
public abstract class AbstractDefaultController {
    @Autowired
    protected UserRepo userRepo;

    @Autowired
    protected TagRepo tagRepo;

    @Autowired
    protected WatchedResourceRepo watchedResourceRepo;

    protected User getCurrentUser() {
        SecurityContext securityContext = this.getSecurityContext();
        if (securityContext == null
                || securityContext.getAuthentication() == null
                || securityContext.getAuthentication().getPrincipal() == null) {
            return null;
        }
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.userRepo.findOne(principal.getName());
    }

    protected boolean isCurrentUserAdmin() {
        return this.isCurrentUserHasRole(RoleType.ADMIN);
    }

    protected boolean isCurrentUserHasRole(RoleType... roleTypes) {
        SecurityContext securityContext = this.getSecurityContext();
        if (securityContext == null
                || securityContext.getAuthentication() == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();
        for (RoleType roleType : roleTypes) {
            if (!authorities.contains(new SimpleGrantedAuthority(roleType.getRealName()))) {
                return false;
            }
        }
        return true;
    }

    protected SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    protected List<Tag> getTagsFromListString(String tagsList) {
        List<String> names = Tag.namesFromStringList(tagsList);
        List<Tag> tags = Lists.newArrayList();
        for (String name : names) {
            Tag tag = this.tagRepo.findFirstByNameContaining(name);
            if (tag == null) {
                continue;
            }
            tags.add(tag);
        }
        return tags;
    }

    protected List<WatchedResource> getFilteredWatchedResources(User user, boolean isAdmin, String isPublic, String tags, String types, Date afterDate) {
        Specification<WatchedResource> specification = new Specification<WatchedResource>() {
            @Override
            public Predicate toPredicate(Root<WatchedResource> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (tags != null) {
                    ListJoin<WatchedResource, Tag> tagJoin = root.joinList("tags");
                    predicates.add(cb.isTrue(tagJoin.get("name").in(Tag.namesFromStringList(tags))));
                }
                if (types != null) {
                    predicates.add(cb.isTrue(root.get("type").in(ResourceType.fromStringList(types))));
                }
                if (isPublic != null) {
                    predicates.add(cb.isTrue(root.get("isPublic")));
                }
                if (afterDate != null) {
                    predicates.add(cb.greaterThan(root.get("updatedResourceAt"), afterDate));
                }
                if (!isAdmin) {
                    ListJoin<WatchedResource, User> usersJoin = root.joinList("users");
                    predicates.add(cb.equal(usersJoin.get("uuid"), user.getUuid()));
                }
                Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[]{}));
                if (isPublic != null) {
                    finalPredicate = cb.or(finalPredicate, cb.isTrue(root.get("isPublic")));
                }
                return finalPredicate;
            }
        };
        return this.watchedResourceRepo.findAll(specification);
    }

    protected ResponseEntity<String> generateEntityFromStatus(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(httpStatus.value() + " " + httpStatus.getReasonPhrase());
    }

}
