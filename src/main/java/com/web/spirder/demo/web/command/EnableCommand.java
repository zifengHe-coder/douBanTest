package com.web.spirder.demo.web.command;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class EnableCommand extends IdCommand {

    @ApiModelProperty("启用/禁用就诊类型")
    @NotNull
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
