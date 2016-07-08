package com.orange.clara.tool.crawlers;

import org.junit.Test;

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
public abstract class AbstractCrawlersTest {
    @Test
    public abstract void when_trying_to_get_new_resource_content_for_the_first_time_it_should_return_all_content_resources() throws Exception;

    @Test
    public abstract void when_trying_to_get_new_resource_content_from_a_date_it_should_return_only_news_ones() throws Exception;

    @Test
    public abstract void when_asking_for_image_it_should_return_image_from_the_resource() throws Exception;
}
