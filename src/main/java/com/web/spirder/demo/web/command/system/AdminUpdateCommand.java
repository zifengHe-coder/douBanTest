package com.web.spirder.demo.web.command.system;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class AdminUpdateCommand {
    @NotNull
    @ApiModelProperty("ID")
    private Long id;

    @Size(max=30)
    @ApiModelProperty("密码，不传表示不更新密码")
    private String password;

    @NotNull
    @Size(max=32)
    @ApiModelProperty("用户账号")
    private String username;

    @NotNull
    @Size(max=128)
    @ApiModelProperty("用户名称")
    private String name;

    @ApiModelProperty("角色ID集合")
    private Set<Long> roleIds;


    @ApiModelProperty("显示的顺序号，用于排序显示")
    private Integer displayOrder;

    @Size(max=500)
    @ApiModelProperty("备注")
    private String remark;

    @NotNull
    @ApiModelProperty("是否启用")
    private boolean enabled = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
