package com.web.spirder.demo.web.command;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class IdCommand {

    @NotNull
    @ApiModelProperty("对象id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
