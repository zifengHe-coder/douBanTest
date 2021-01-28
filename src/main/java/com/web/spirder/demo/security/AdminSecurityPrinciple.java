package com.web.spirder.demo.security;

import com.idaoben.web.token_auth.oauth2.UserInfo;
import com.web.spirder.demo.dao.entity.Admin;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.ZonedDateTime;
import java.util.Collection;

@ApiModel
public class AdminSecurityPrinciple extends User implements UserInfo {
    private static final long serialVersionUID = 6949531631991786733L;

    private Long id;

    private String screenName;

    private ZonedDateTime lastLoginTime;

    public AdminSecurityPrinciple(String username, String password, boolean enable, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enable, true, true, true, authorities);
    }

    @ApiModelProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty("登录名")
    public String getLoginName() {
        return super.getUsername();
    }

    @ApiModelProperty("显示名称")
    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @ApiModelProperty("上次登录时间")
    public ZonedDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(ZonedDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public static AdminSecurityPrinciple fromAdmin(Admin admin){
        AdminSecurityPrinciple adminSecurityDto = new AdminSecurityPrinciple(admin.getUsername(), admin.getPassword(), admin.isEnabled(), RoleUtils.getMergedAutorities(admin.getRoles()));
        adminSecurityDto.setId(admin.getId());
        adminSecurityDto.setScreenName(admin.getName());
        adminSecurityDto.setLastLoginTime(admin.getLastLoginTime());
        return adminSecurityDto;
    }

    @Override
    public Object getUid() {
        return id;
    }
}
