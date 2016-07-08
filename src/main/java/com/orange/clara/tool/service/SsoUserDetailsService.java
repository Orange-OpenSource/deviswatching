package com.orange.clara.tool.service;

import com.google.common.collect.Maps;
import com.orange.clara.tool.model.EnumOauthProvider;
import com.orange.clara.tool.model.Role;
import com.orange.clara.tool.model.RoleType;
import com.orange.clara.tool.model.User;
import com.orange.clara.tool.repos.RoleRepo;
import com.orange.clara.tool.repos.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
@Transactional
public class SsoUserDetailsService extends UserInfoTokenServices implements ResourceServerTokenServices {
    private static Logger logger = LoggerFactory.getLogger(SsoUserDetailsService.class);

    @Autowired
    @Qualifier("oauthProvider")
    private EnumOauthProvider oauthProvider;

    @Autowired
    @Qualifier("usernameDefaultAdmin")
    private String usernameDefaultAdmin;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;

    private String finalClientId;

    public SsoUserDetailsService(String userInfoEndpointUrl, String clientId) {
        super(userInfoEndpointUrl, clientId);
        this.finalClientId = clientId;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        OAuth2Authentication oAuth2Authentication = super.loadAuthentication(accessToken);
        UsernamePasswordAuthenticationToken userAuthentication = (UsernamePasswordAuthenticationToken) oAuth2Authentication.getUserAuthentication();
        User user = this.getUser((Map<String, Object>) userAuthentication.getDetails());
        Principal principal = () -> user.getUuid();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal, "N/A", this.getGrantedAuthorities(user.getRoles()));
        token.setDetails(this.generateDetailsFromUser(user));
        OAuth2Request request = new OAuth2Request(null, this.finalClientId, null, true, null,
                null, null, null, null);

        return new OAuth2Authentication(request, token);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return super.readAccessToken(accessToken);
    }

    @Transactional
    private User getUser(Map<String, Object> userInfos) {
        switch (oauthProvider) {
            case GITHUB:
                return this.getUserGithub(userInfos);
            default:
                return this.getUserUaa(userInfos);
        }
    }

    private User getUserUaa(Map<String, Object> userInfos) {
        if (this.userRepo.exists((String) userInfos.get("user_id"))) {
            return this.userRepo.findOne((String) userInfos.get("user_id"));
        }
        return this.createUser((String) userInfos.get("user_id"), (String) userInfos.get("user_name"), (String) userInfos.get("email"));
    }

    private User getUserGithub(Map<String, Object> userInfos) {
        String uuid = String.valueOf((Integer) userInfos.get("id"));
        if (this.userRepo.exists(uuid)) {
            User user = this.userRepo.findOne(uuid);
            user.updateLastWatch();
            this.userRepo.save(user);
            return user;
        }
        return this.createUser(uuid, (String) userInfos.get("login"), (String) userInfos.get("email"));
    }

    private User createUser(String uuid, String login, String email) {
        User user = new User(uuid, login, email);
        user.addRole(this.generateRole(login));
        this.userRepo.save(user);
        return user;
    }

    private Role generateRole(String username) {
        if (usernameDefaultAdmin == null) {
            return this.generateRoleByCount();
        }
        if (usernameDefaultAdmin.equals(username)) {
            return this.getAdminRole();
        } else {
            return this.getUserRole();
        }
    }

    private Role generateRoleByCount() {
        if (this.userRepo.count() > 0) {
            return this.getUserRole();
        } else {
            return this.getAdminRole();
        }
    }

    private Role getUserRole() {
        return roleRepo.findFirstByName(RoleType.USER);
    }

    private Role getAdminRole() {
        return roleRepo.findFirstByName(RoleType.ADMIN);
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName().getRealName()));
        }
        return authorities;
    }

    private Map<String, Object> generateDetailsFromUser(User user) {
        Map<String, Object> details = Maps.newHashMap();
        details.put("email", user.getEmail());
        details.put("name", user.getName());
        details.put("last_watch", user.getLastWatch());
        return details;
    }
}
