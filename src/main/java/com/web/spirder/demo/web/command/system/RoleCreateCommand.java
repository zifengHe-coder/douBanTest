package com.web.spirder.demo.web.command.system;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author retime
 */
public class RoleCreateCommand {

    @NotNull
    @Length(max = 20)
    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @Length(max = 500)
    @ApiModelProperty(value = "备注")
    private String remark;
    @NotNull
    @ApiModelProperty(value = "编码")
    private String codeNo;

    @ApiModelProperty("显示的顺序号，用于排序显示")
    private Integer displayOrder;

    @NotNull
    @ApiModelProperty("是否启用")
    private boolean enabled = true;


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCodeNo() {
        return codeNo;
    }

    public void setCodeNo(String codeNo) {
        this.codeNo = codeNo;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}
