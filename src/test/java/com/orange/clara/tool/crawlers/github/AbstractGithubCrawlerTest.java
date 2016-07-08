package com.orange.clara.tool.crawlers.github;

import com.orange.clara.tool.crawlers.AbstractCrawlersTest;
import org.eclipse.egit.github.core.User;

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
public abstract class AbstractGithubCrawlerTest extends AbstractCrawlersTest {
    protected static final String AVATAR_URL = "http://test.com/myavatar.png";
    protected final static String username = "user";
    protected User user;

    protected User generateUser() {
        User user = new User();
        user.setLogin(username);
        user.setAvatarUrl(AVATAR_URL);
        return user;
    }
}
