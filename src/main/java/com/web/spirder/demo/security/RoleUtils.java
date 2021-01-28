package com.web.spirder.demo.security;

import com.web.spirder.demo.dao.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;

public class RoleUtils {
    /**
     * 将角色集合转化为权限集合
     */
    public static Collection<? extends GrantedAuthority> getMergedAutorities(Collection<Role> roles) {
        HashSet<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.addAll(AuthorityUtils.commaSeparatedStringToAuthorityList(
                    StringUtils.collectionToCommaDelimitedString(role.getPermissions())));
        }
        return authorities;
    }

    /**
     * 将权限的字符描述集合（逗号分隔）转化为权限集合
     */
    public static Collection<? extends GrantedAuthority> getMergedAutorities(String roles) {
        HashSet<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(AuthorityUtils.commaSeparatedStringToAuthorityList(roles));
        return authorities;
    }
}
