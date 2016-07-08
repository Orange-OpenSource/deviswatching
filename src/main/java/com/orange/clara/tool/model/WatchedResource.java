package com.orange.clara.tool.model;

import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.Calendar;
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
 * Date: 24/06/2016
 */
@Entity
public class WatchedResource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "watched_resource_id")
    private int id;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "watchedResources")
    private List<User> users;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "watched_resource_tag", joinColumns = {
            @JoinColumn(name = "watched_resource_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tags;

    private String link;

    private String triggerOn;

    private String image;

    private String title;

    private boolean isPublic = true;

    @Column(name = "updated_resource_at")
    private Date updatedResourceAt;

    @OneToMany(mappedBy = "watchedResource", fetch = FetchType.EAGER)
    private List<ContentResource> contentResources;

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    private boolean locked = false;

    public WatchedResource() {
        contentResources = Lists.newArrayList();
        users = Lists.newArrayList();
        tags = Lists.newArrayList();
    }

    public WatchedResource generatePartial() {
        WatchedResource partialWatchedResource = new WatchedResource();
        partialWatchedResource.setId(this.getId());
        partialWatchedResource.setTitle(this.getTitle());
        partialWatchedResource.setLink(this.getLink());
        partialWatchedResource.setUpdatedResourceAt(this.getUpdatedResourceAt());
        return partialWatchedResource;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        if (this.users.contains(user)) {
            return;
        }
        this.users.add(user);
        user.addWatchedResource(this);
    }

    public void removeUser(User user) {
        if (!this.users.contains(user)) {
            return;
        }
        this.users.remove(user);
        user.removeWatchedResource(this);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTriggerOn() {
        return triggerOn;
    }

    public void setTriggerOn(String triggerOn) {
        this.triggerOn = triggerOn;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public List<ContentResource> getContentResources() {
        return contentResources;
    }

    public void setContentResources(List<ContentResource> contentResources) {
        this.contentResources = contentResources;
    }

    public void addContentResource(ContentResource contentResource) {
        if (this.contentResources.contains(contentResource)) {
            return;
        }
        contentResource.setWatchedResource(this);
        this.contentResources.add(contentResource);
    }

    public void removeContentResource(ContentResource contentResource) {
        if (!this.contentResources.contains(contentResource)) {
            return;
        }
        contentResource.setWatchedResource(null);
        this.contentResources.remove(contentResource);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        if (this.tags.contains(tag)) {
            return;
        }
        tag.addWatchedResource(this);
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        if (!this.tags.contains(tag)) {
            return;
        }
        tag.removeWatchedResource(this);
        this.tags.remove(tag);
    }

    public Date getUpdatedResourceAt() {
        return updatedResourceAt;
    }

    public void setUpdatedResourceAt(Date updatedResourceAt) {
        this.updatedResourceAt = updatedResourceAt;
    }

    public void refreshUpdate() {
        this.updatedResourceAt = Calendar.getInstance().getTime();
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean hasUser(User user) {
        return this.users.contains(user);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WatchedResource)) return false;

        WatchedResource that = (WatchedResource) o;

        return id == that.id;

    }

    @Override
    public String toString() {
        return "WatchedResource{" +
                "id=" + id +
                ", title=" + title +
                ", tags=" + tags +
                ", link='" + link + '\'' +
                ", triggerOn='" + triggerOn + '\'' +
                ", updatedResourceAt=" + updatedResourceAt +
                ", contentResources=" + contentResources +
                ", type=" + type +
                ", image=" + image +
                ", is_public=" + isPublic +
                '}';
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
