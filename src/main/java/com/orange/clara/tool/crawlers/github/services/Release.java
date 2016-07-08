package com.orange.clara.tool.crawlers.github.services;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 27/06/2016
 */

import com.google.gson.annotations.SerializedName;
import org.eclipse.egit.github.core.User;

import java.io.Serializable;
import java.util.Date;

public class Release implements Serializable {

    private static final long serialVersionUID = 1L;

    private String url;

    private String htmlUrl;

    private String assetsUrl;

    private String uploadUrl;

    private String tarballUrl;

    private String zipballUrl;

    private long id;

    private String tagName;

    private String targetCommitish;

    private String name;

    private String body;

    @SerializedName("draft")
    private boolean isDraft;

    @SerializedName("prerelease")
    private boolean isPrerelease;

    private Date createdAt;

    private Date publishedAt;

    private User author;

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     * @return this release
     */
    public Release setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @return htmlUrl
     */
    public String getHtmlUrl() {
        return htmlUrl;
    }

    /**
     * @param htmlUrl
     * @return this release
     */
    public Release setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
        return this;
    }

    /**
     * @return assetsUrl
     */
    public String getAssetsUrl() {
        return assetsUrl;
    }

    /**
     * @param assetsUrl
     * @return this release
     */
    public Release setAssetsUrl(String assetsUrl) {
        this.assetsUrl = assetsUrl;
        return this;
    }

    /**
     * @return uploadUrl
     */
    public String getUploadUrl() {
        return uploadUrl;
    }

    /**
     * @param uploadUrl
     * @return this release
     */
    public Release setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
        return this;
    }

    /**
     * @return tarballUrl
     */
    public String getTarballUrl() {
        return tarballUrl;
    }

    /**
     * @param tarballUrl
     * @return this release
     */
    public Release setTarballUrl(String tarballUrl) {
        this.tarballUrl = tarballUrl;
        return this;
    }

    /**
     * @return zipballUrl
     */
    public String getZipballUrl() {
        return zipballUrl;
    }

    /**
     * @param zipballUrl
     * @return this release
     */
    public Release setZipballUrl(String zipballUrl) {
        this.zipballUrl = zipballUrl;
        return this;
    }

    /**
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     * @return this release
     */
    public Release setId(long id) {
        this.id = id;
        return this;
    }

    /**
     * @return tagName
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * @param tagName
     * @return this release
     */
    public Release setTagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    /**
     * @return targetCommitish
     */
    public String getTargetCommitish() {
        return targetCommitish;
    }

    /**
     * @param targetCommitish
     * @return this release
     */
    public Release setTargetCommitish(String targetCommitish) {
        this.targetCommitish = targetCommitish;
        return this;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     * @return this release
     */
    public Release setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body
     * @return this release
     */
    public Release setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * @return isDraft
     */
    public boolean isDraft() {
        return isDraft;
    }

    /**
     * @param isDraft
     * @return this release
     */
    public Release setDraft(boolean isDraft) {
        this.isDraft = isDraft;
        return this;
    }

    /**
     * @return isPrerelease
     */
    public boolean isPrerelease() {
        return isPrerelease;
    }

    /**
     * @param isPrerelease
     * @return this release
     */
    public Release setPrerelease(boolean isPrerelease) {
        this.isPrerelease = isPrerelease;
        return this;
    }

    /**
     * @return createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt
     * @return this release
     */
    public Release setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * @return publishedAt
     */
    public Date getPublishedAt() {
        return publishedAt;
    }

    /**
     * @param publishedAt
     * @return this release
     */
    public Release setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    /**
     * @return author
     */
    public User getAuthor() {
        return author;
    }

    /**
     * @param author
     * @return this release
     */
    public Release setAuthor(User author) {
        this.author = author;
        return this;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (htmlUrl != null ? htmlUrl.hashCode() : 0);
        result = 31 * result + (assetsUrl != null ? assetsUrl.hashCode() : 0);
        result = 31 * result + (uploadUrl != null ? uploadUrl.hashCode() : 0);
        result = 31 * result + (tarballUrl != null ? tarballUrl.hashCode() : 0);
        result = 31 * result + (zipballUrl != null ? zipballUrl.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (tagName != null ? tagName.hashCode() : 0);
        result = 31 * result + (targetCommitish != null ? targetCommitish.hashCode() : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (isDraft ? 1 : 0);
        result = 31 * result + (isPrerelease ? 1 : 0);
        result = 31 * result + createdAt.hashCode();
        result = 31 * result + (publishedAt != null ? publishedAt.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Release)) return false;

        Release release = (Release) o;

        if (id != release.id) return false;
        if (isDraft != release.isDraft) return false;
        if (isPrerelease != release.isPrerelease) return false;
        if (url != null ? !url.equals(release.url) : release.url != null) return false;
        if (htmlUrl != null ? !htmlUrl.equals(release.htmlUrl) : release.htmlUrl != null) return false;
        if (assetsUrl != null ? !assetsUrl.equals(release.assetsUrl) : release.assetsUrl != null) return false;
        if (uploadUrl != null ? !uploadUrl.equals(release.uploadUrl) : release.uploadUrl != null) return false;
        if (tarballUrl != null ? !tarballUrl.equals(release.tarballUrl) : release.tarballUrl != null) return false;
        if (zipballUrl != null ? !zipballUrl.equals(release.zipballUrl) : release.zipballUrl != null) return false;
        if (tagName != null ? !tagName.equals(release.tagName) : release.tagName != null) return false;
        if (targetCommitish != null ? !targetCommitish.equals(release.targetCommitish) : release.targetCommitish != null)
            return false;
        if (!name.equals(release.name)) return false;
        if (body != null ? !body.equals(release.body) : release.body != null) return false;
        if (!createdAt.equals(release.createdAt)) return false;
        if (publishedAt != null ? !publishedAt.equals(release.publishedAt) : release.publishedAt != null) return false;
        return author != null ? author.equals(release.author) : release.author == null;

    }

    @Override
    public String toString() {
        return "Release{" +
                "url='" + url + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", assetsUrl='" + assetsUrl + '\'' +
                ", uploadUrl='" + uploadUrl + '\'' +
                ", tarballUrl='" + tarballUrl + '\'' +
                ", zipballUrl='" + zipballUrl + '\'' +
                ", id=" + id +
                ", tagName='" + tagName + '\'' +
                ", targetCommitish='" + targetCommitish + '\'' +
                ", name='" + name + '\'' +
                ", body='" + body + '\'' +
                ", isDraft=" + isDraft +
                ", isPrerelease=" + isPrerelease +
                ", createdAt=" + createdAt +
                ", publishedAt=" + publishedAt +
                ", author=" + author +
                '}';
    }
}