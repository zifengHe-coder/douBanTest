package com.web.spirder.demo.web.command.system;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class SetRolePermissionsCommand{

    @NotNull
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("权限集合")
    private Set<String> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }




}
