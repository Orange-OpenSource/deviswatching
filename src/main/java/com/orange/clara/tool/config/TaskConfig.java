package com.orange.clara.tool.config;

import com.orange.clara.tool.schedulers.tasks.RefreshTask;
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
 * Date: 29/06/2016
 */
@Configuration
public class TaskConfig {
    @Bean
    public RefreshTask refreshTask() {
        return new RefreshTask();
    }
}
