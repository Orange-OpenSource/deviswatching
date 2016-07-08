package com.orange.clara.tool.model;

import com.google.common.collect.Lists;

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
public enum ResourceType {
    GITHUB_RELEASES("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png"),
    GITHUB_REPOSITORIES("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png"),
    TWITTER("https://g.twimg.com/Twitter_logo_blue.png"),
    RSS("https://www.rss.com/assets/img/logo-rss-transparent-5677094196f04f252d0f16401b30b727.png"),
    YOUTUBE("https://www.youtube.com/yt/brand/media/image/YouTube-logo-full_color.png"),
    BLOG("http://www.andrewlaws.com/wp-content/uploads/2013/05/Andrew-Laws-Associates-BLOG-square-logo.png"),
    SEARCH("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/Logo_2013_Google.png/320px-Logo_2013_Google.png"),
    GOOGLE_DRIVE("http://www.google.com/drive/images/drive/logo-drive.png");
    private String defaultImageUrl;

    ResourceType(String defaultImageUrl) {
        this.defaultImageUrl = defaultImageUrl;
    }

    public static List<ResourceType> fromStringList(String typeList) {
        String[] types = typeList.split(",");
        List<ResourceType> resourceTypes = Lists.newArrayList();
        for (String type : types) {
            resourceTypes.add(ResourceType.valueOf(type.trim().toUpperCase()));
        }
        return resourceTypes;
    }

    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }
}
