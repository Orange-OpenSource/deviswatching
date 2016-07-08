package com.orange.clara.tool.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
 * Date: 25/06/2016
 */
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tag_id")
    private int id;

    private String name;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    @JsonBackReference
    private List<WatchedResource> watchedResources;

    public Tag() {
        watchedResources = Lists.newArrayList();
    }

    public Tag(String name) {
        this();
        this.name = name.toLowerCase();
    }

    public static List<String> namesFromStringList(String nameList) {
        List<String> names = Lists.newArrayList();
        String[] nameArray = nameList.split(",");
        for (String name : nameArray) {
            names.add(name.trim().toLowerCase());
        }
        return names;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
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

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;

        Tag tag = (Tag) o;

        if (id != tag.id) return false;
        return name.equals(tag.name);

    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
