package com.web.spirder.demo.web.command.system;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

public class AdminCreateCommand {

    @NotNull
    @Size(min=3,max=20,message = "用户账号长度限制在3-20字符")
    @Pattern(regexp="^[a-zA-Z]{1}([a-zA-Z0-9]){2,19}$",message = "用户账号支持数字、字母组合，必须以字母开头")
    @ApiModelProperty("用户账号")
    private String username;

    @Size(min=1,max=10,message = "用户名称长度限制在1-10字符")
    @NotNull
    @ApiModelProperty("用户名称")
    private String name;

    @NotNull
    @Size(min=6,max=20,message = "密码长度限制在6-20字符")
    @Pattern(regexp="^[a-zA-Z0-9]{6,20}$",message = "密码支持数字、字母组合")
    @ApiModelProperty("密码")
    private String password;

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


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

