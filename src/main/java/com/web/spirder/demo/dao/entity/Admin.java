package com.web.spirder.demo.dao.entity;

import com.idaoben.web.common.entity.Description;
import com.idaoben.web.common.entity.TrackableObject;
import com.web.spirder.demo.dao.entity.utils.TrackableObjectSnowFlake;


import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_admin")
@NamedEntityGraph(name = "Admin.detailed",attributeNodes = @NamedAttributeNode(value = "roles"))
@Description("管理员")
public class Admin extends TrackableObject {
    private static final long serialVersionUID = 1313368504779178204L;

    @Column(name = "username",length = 32,nullable = false,unique = true)
    @Description("用户账号")
    private String username;

    @Column(name = "display_order")
    @Description("顺序号")
    private Integer displayOrder;

    /**
     * 最后登录日期
     */
    @Column
    @Description("最后登录日期")
    private ZonedDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    @Column
    @Description("最后登录IP")
    private String lastLoginIP;

    /**
     * 是否启用
     */
    @Column(name = "enabled", nullable = false)
    @Description("是否启用")
    private boolean enabled = true;

    /**
     * 管理员密码
     */
    @Column(name = "password", length = 255, nullable = false)
    @Description("管理员加密密码")
    private String password;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    @Description("备注")
    private String remark;

    /**
     * 是否超管
     */
    @Column(name = "admin", nullable = false)
    @Description("是否超管")
    private boolean adminFlag = false;

    /**
     * 角色集
     */
    @ManyToMany
    @JoinTable(name = "t_admin_has_role", joinColumns = @JoinColumn(name = "admin_id") , inverseJoinColumns = @JoinColumn(name = "role_id") )
    @OrderBy("id desc")
    @Description("角色集")
    private Set<Role> roles = new HashSet<>();

    @Column(name = "name")
    @Description("用户名")
    private String name;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ZonedDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(ZonedDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(boolean adminFlag) {
        this.adminFlag = adminFlag;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
