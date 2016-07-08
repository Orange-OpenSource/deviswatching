package com.orange.clara.tool.model;

import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.List;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 30/06/2016
 */
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Enumerated(EnumType.STRING)
    private RoleType name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Role() {
        users = Lists.newArrayList();
    }

    public Role(RoleType name) {
        this();
        this.name = name;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void removeUser(User user) {
        if (!this.users.contains(user)) {
            return;
        }
        this.users.remove(user);
    }

    public void addUser(User user) {
        if (this.users.contains(user)) {
            return;
        }
        this.users.add(user);
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        if (id != role.id) return false;
        return name.equals(role.name);

    }
}
