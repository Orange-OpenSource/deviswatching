package com.orange.clara.tool.controllers.api;

import com.orange.clara.tool.model.ResourceType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 01/07/2016
 */
@RestController
@RequestMapping("/api/resourceType")
public class ResourceTypeController {

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<ResourceType> getResourceTypes() {
        return Arrays.asList(ResourceType.values());
    }
}
