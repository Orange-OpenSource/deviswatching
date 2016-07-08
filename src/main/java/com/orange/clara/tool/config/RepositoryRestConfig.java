package com.orange.clara.tool.config;

import com.orange.clara.tool.model.ContentResource;
import com.orange.clara.tool.model.Tag;
import com.orange.clara.tool.model.User;
import com.orange.clara.tool.model.WatchedResource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 08/07/2016
 */
@Configuration
public class RepositoryRestConfig extends RepositoryRestConfigurerAdapter {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(WatchedResource.class, Tag.class, ContentResource.class, User.class);
    }

    @Override
    public void configureConversionService(ConfigurableConversionService conversionService) {
    }

}
