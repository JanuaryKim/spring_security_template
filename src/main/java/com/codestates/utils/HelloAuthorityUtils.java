package com.codestates.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HelloAuthorityUtils {

    @Value("${mail.address.admin}")
    private String adminMailAddress;

    //유저의 권한까지 포함함.
    private final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");

    private final List<GrantedAuthority> USER_ROLES =AuthorityUtils.createAuthorityList("ROLE_USER");

    private final List<String> ADMIN_ROLES_STRING = List.of("ROLE_ADMIN", "ROLE_USER");
    private final List<String> USER_ROLES_STRING = List.of("ROLE_USER");

    public List<GrantedAuthority> createAuthorities(String email) {

        if(email.equals(adminMailAddress))
        {
            return ADMIN_ROLES;
        }

        return USER_ROLES;
    }

    public List<GrantedAuthority> createAuthorities(List<String> roles) {

        List<GrantedAuthority> grantedAuthorities = roles.stream().map(
                str -> new SimpleGrantedAuthority("ROLE_" + str)).collect(Collectors.toList());

        return grantedAuthorities;
    }

    public List<String> createRole(String email) {
        if(email.equals(adminMailAddress))
        {
            return ADMIN_ROLES_STRING;
        }

        return USER_ROLES_STRING;

    }
}
