package com.orange.clara.tool.controllers.api;

import com.github.slugify.Slugify;
import com.google.common.collect.Lists;
import com.orange.clara.tool.controllers.AbstractDefaultController;
import com.orange.clara.tool.controllers.RssController;
import com.orange.clara.tool.crawlers.Crawler;
import com.orange.clara.tool.crawlers.CrawlerFactory;
import com.orange.clara.tool.exceptions.CrawlerGetContentException;
import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.Tag;
import com.orange.clara.tool.model.User;
import com.orange.clara.tool.model.WatchedResource;
import com.orange.clara.tool.repos.ContentResourceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 29/06/2016
 */
@RepositoryRestController
@RequestMapping("/api/watchedResource")
public class WatchedResourcesController extends AbstractDefaultController {

    @Autowired
    protected ContentResourceRepo contentResourceRepo;

    @Autowired
    @Qualifier("slugify")
    private Slugify slugify;

    @Autowired
    private CrawlerFactory crawlerFactory;

    @RequestMapping(method = RequestMethod.POST, value = "")
    public
    @ResponseBody
    ResponseEntity<?> register(@RequestBody WatchedResource watchedResource) throws CrawlerGetContentException {
        User user = this.getCurrentUser();
        watchedResource.setLocked(true);
        watchedResource.addUser(user);
        Crawler crawler = this.crawlerFactory.findCrawler(watchedResource.getType());
        this.createOrLinkTag(watchedResource);
        watchedResource.setTitle(crawler.generateTitle(watchedResource));
        watchedResource.setImage(crawler.getImage(watchedResource));
        watchedResource = this.watchedResourceRepo.save(watchedResource);
        List<ContentResource> contentResources = crawler.getLastContent(watchedResource);
        for (ContentResource contentResource : contentResources) {
            watchedResource.addContentResource(contentResource);
            this.contentResourceRepo.save(contentResource);
        }
        watchedResource.setUpdatedResourceAt(Calendar.getInstance().getTime());
        watchedResource.setLocked(false);
        watchedResource = this.watchedResourceRepo.save(watchedResource);
        return ResponseEntity.ok(this.generateResource(watchedResource));
    }

    private void createOrLinkTag(WatchedResource watchedResource) {
        List<Tag> tags = Lists.newArrayList();
        Tag tagToAdd = null;
        for (Tag tag : watchedResource.getTags()) {
            if (tag.getId() != 0) {
                tagToAdd = this.tagRepo.findOne(tag.getId());
            } else {
                tagToAdd = tag;
            }
            tags.add(tagToAdd);
        }
        watchedResource.setTags(tags);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<?> getWatchedResource(@PathVariable Integer id) {
        User user = this.getCurrentUser();
        WatchedResource watchedResource = this.watchedResourceRepo.findOne(id);
        if (!watchedResource.isPublic() && !watchedResource.hasUser(user)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.generateResource(watchedResource));
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<?> getWatchedResources(
            @RequestParam(value = "public", required = false) String isPublic,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "types", required = false) String types,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(value = "after_date", required = false) Date afterDate
    ) throws CrawlerGetContentException {
        User user = this.getCurrentUser();
        List<WatchedResource> watchedResources = this.getFilteredWatchedResources(user, this.isCurrentUserAdmin(), isPublic, tags, types, afterDate);
        Resources<Resource<WatchedResource>> resources = new Resources<Resource<WatchedResource>>(this.generateResources(watchedResources));
        resources.add(linkTo(methodOn(WatchedResourcesController.class).getWatchedResources(isPublic, tags, types, afterDate)).withSelfRel());
        return ResponseEntity.ok(resources);
    }

    public List<Resource<WatchedResource>> generateResources(List<WatchedResource> watchedResources) {
        List<Resource<WatchedResource>> resourcesWatchedResource = Lists.newArrayList();
        for (WatchedResource watchedResource : watchedResources) {
            resourcesWatchedResource.add(this.generateResource(watchedResource));
        }
        return resourcesWatchedResource;
    }

    private Resource<WatchedResource> generateResource(WatchedResource watchedResource) {
        Resource<WatchedResource> resource = new Resource<>(watchedResource);
        try {
            resource.add(linkTo(methodOn(WatchedResourcesController.class).getWatchedResource(watchedResource.getId())).withSelfRel());
            Link linkNoSlug = linkTo(methodOn(RssController.class).getFeed(watchedResource.getId())).withRel("rss");
            Link linkSlug = new Link(linkNoSlug.getHref().replace("*", "") + "-" + this.slugify.slugify(watchedResource.getTitle()), linkNoSlug.getRel());
            resource.add(linkSlug);
        } catch (Exception e) {
        }
        return resource;
    }
}
