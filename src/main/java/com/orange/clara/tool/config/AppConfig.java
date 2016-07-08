package com.orange.clara.tool.config;

import com.github.slugify.Slugify;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.parser.Parser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

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
@Configuration
public class AppConfig {
    @Value("#{${use.ssl:false} ? 'https://' : 'http://'}")
    private String appProtocol;

    @Value("${vcap.application.uris[0]:localhost:8081}")
    private String appUri;

    @Value("${vcap.application.name:deviswatching}")
    private String appName;
    @Value("${deviswatching.ui.url:#{null}}")
    private String uiUrl;

    @Value("${username.default.admin:#{null}}")
    private String usernameDefaultAdmin;

    @Bean
    public Slugify slugify() throws IOException {
        return new Slugify();
    }

    @Bean
    public Executor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public String appUrl() {
        return appProtocol + this.appUri;
    }

    @Bean
    public String appUri() {
        return this.appUri;
    }

    @Bean
    public String appName() {
        return this.appName;
    }

    @Bean
    public Parser markdownParser() {
        return Parser.builder().extensions(markdownExtension()).build();
    }

    @Bean
    public HtmlRenderer markdownHtmlRenderer() {
        return HtmlRenderer.builder().extensions(markdownExtension()).build();
    }

    @Bean
    public List<Extension> markdownExtension() {
        return Arrays.asList(TablesExtension.create(), AutolinkExtension.create(), StrikethroughExtension.create());
    }

    @Bean
    public URL uiUrl() throws MalformedURLException {
        if (uiUrl == null) {
            return null;
        }
        return new URL(uiUrl);
    }

    @Bean
    public String usernameDefaultAdmin() {
        return usernameDefaultAdmin;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedHeaders("x-auth-token", "x-requested-with");
            }
        };
    }
}
