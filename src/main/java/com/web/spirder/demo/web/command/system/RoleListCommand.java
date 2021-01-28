package com.web.spirder.demo.web.command.system;

import io.swagger.annotations.ApiModelProperty;

public class RoleListCommand {

    @ApiModelProperty("查询关键字，账号/姓名")
    private String keyword;

    @ApiModelProperty("是否启用 0-禁用 1-启用")
    private Integer enabled;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }






}
