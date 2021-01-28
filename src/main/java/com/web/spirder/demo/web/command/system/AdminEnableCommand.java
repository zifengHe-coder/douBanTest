package com.web.spirder.demo.web.command.system;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class AdminEnableCommand {

    @NotNull
    @ApiModelProperty("ID")
    private Long id;

    @NotNull
    @ApiModelProperty("是否启用")
    private boolean enable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
