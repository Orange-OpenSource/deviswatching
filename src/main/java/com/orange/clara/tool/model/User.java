package com.orange.clara.tool.model;

import com.google.common.collect.Lists;

import javax.persistence.*;
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
@Entity
public class User {
    @Id
    @Column(name = "user_uuid")
    protected String uuid;

    protected String name;

    protected String email;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_uui"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    protected List<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_watched_resource", joinColumns = {
            @JoinColumn(name = "user_uuid")},
            inverseJoinColumns = {@JoinColumn(name = "watched_resource_id")})
    protected List<WatchedResource> watchedResources;

    public User() {
        watchedResources = Lists.newArrayList();
        roles = Lists.newArrayList();
    }

    public User(String uuid, String name, String email) {
        this();
        this.uuid = uuid;
        this.name = name;
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<WatchedResource> getWatchedResources() {
        return watchedResources;
    }

    public void setWatchedResources(List<WatchedResource> watchedResources) {
        this.watchedResources = watchedResources;
    }

    public void addWatchedResource(WatchedResource watchedResource) {
        if (this.watchedResources.contains(watchedResource)) {
            return;
        }
        this.watchedResources.add(watchedResource);
    }

    public void removeWatchedResource(WatchedResource watchedResource) {
        if (!this.watchedResources.contains(watchedResource)) {
            return;
        }
        this.watchedResources.remove(watchedResource);
    }

    public User generatePartial() {
        User partial = new User();
        partial.setUuid(this.getUuid());
        partial.setName(this.getName());
        partial.setEmail(this.getEmail());
        return partial;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        if (this.roles.contains(role)) {
            return;
        }
        this.roles.add(role);
        role.addUser(this);
    }

    public void removeRole(Role role) {
        if (!this.roles.contains(role)) {
            return;
        }
        this.roles.remove(role);
        role.removeUser(this);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return uuid.equals(user.uuid);

    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", watchedResources=" + watchedResources +
                '}';
    }
}
