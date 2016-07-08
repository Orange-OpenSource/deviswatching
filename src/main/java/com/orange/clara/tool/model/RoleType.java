package com.orange.clara.tool.model;

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
public enum RoleType {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");
    private String realName;

    RoleType(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }
}
