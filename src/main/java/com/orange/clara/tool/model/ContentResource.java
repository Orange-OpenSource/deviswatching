package com.orange.clara.tool.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

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
public class ContentResource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "content_resource_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "watched_resource_id")
    private WatchedResource watchedResource;

    @Type(type = "text")
    private String description;

    private String title;
    private String link;
    private String author;
    private String image;
    private Date date;


    public ContentResource() {
        this.date = Calendar.getInstance().getTime();
    }

    public ContentResource generatePartial() {
        ContentResource partialContentResource = new ContentResource();
        partialContentResource.setId(this.getId());
        partialContentResource.setTitle(this.getTitle());
        partialContentResource.setAuthor(this.getAuthor());
        partialContentResource.setLink(this.getLink());
        partialContentResource.setDate(this.getDate());
        return partialContentResource;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WatchedResource getWatchedResource() {
        return watchedResource;
    }

    public void setWatchedResource(WatchedResource watchedResource) {
        this.watchedResource = watchedResource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + date.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContentResource)) return false;

        ContentResource that = (ContentResource) o;

        if (id != that.id) return false;
        if (!title.equals(that.title)) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        return date.equals(that.date);

    }

    @Override
    public String toString() {
        return "ContentResource{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", author='" + author + '\'' +
                ", image='" + image + '\'' +
                ", date=" + date +
                '}';
    }
}
