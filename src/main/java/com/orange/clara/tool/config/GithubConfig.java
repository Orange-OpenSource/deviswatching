package com.orange.clara.tool.config;

import com.orange.clara.tool.crawlers.github.client.ProxyGithubClient;
import com.orange.clara.tool.crawlers.github.services.CustomRepositoryService;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
@Configuration
public class GithubConfig {

    @Value("${crawler.github.token:#{null}}")
    public String githubToken;

    @Bean
    public GitHubClient gitHubClient() {
        GitHubClient client = new ProxyGithubClient();
        if (githubToken != null) {
            client.setOAuth2Token(githubToken);
        }
        return client;
    }

    @Bean
    public CustomRepositoryService customRepositoryService() {
        return new CustomRepositoryService(this.gitHubClient());
    }

    @Bean
    public UserService userService() {
        return new UserService(this.gitHubClient());
    }
}
