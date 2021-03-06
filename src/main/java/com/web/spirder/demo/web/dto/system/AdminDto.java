package com.web.spirder.demo.web.dto.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.idaoben.utils.dto_assembler.annotation.EnableAssembling;
import com.idaoben.utils.dto_assembler.annotation.Mapping;
import com.idaoben.utils.dto_assembler.annotation.TypeMapping;
import com.web.spirder.demo.dao.entity.Admin;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.ZonedDateTime;
import java.util.List;

/**
 *
 * 管理员 Dto接口
 * Generated by DAOBEN CODE GENERATOR
 * @author  Daoben Code Generator
 */
@ApiModel
@EnableAssembling(mappings = @TypeMapping(from = Admin.class))
public interface AdminDto {

    @ApiModelProperty("ID")
    @Mapping
    @JsonFormat(shape = JsonFormat.Shape.STRING) Long getId();


    @ApiModelProperty("用户账号")
    @Mapping
    String getUsername();

    @ApiModelProperty("用户名称")
    @Mapping
    String getName();

    @ApiModelProperty("最后登录日期")
    @Mapping
    ZonedDateTime getLastLoginTime();

    @ApiModelProperty("最后登录IP")
    @Mapping
    String getLastLoginIP();

    @ApiModelProperty("是否启用")
    @Mapping
    boolean getEnabled();

    @ApiModelProperty("管理员加密密码")
    @Mapping
    String getPassword();

    @ApiModelProperty("显示的顺序号，用于排序显示")
    @Mapping
    Integer getDisplayOrder();

    @ApiModelProperty("备注")
    @Mapping
    String getRemark();

    @ApiModelProperty("是否超管")
    @Mapping
    boolean getAdminFlag();

    @ApiModelProperty("角色")
    @Mapping
    List<RoleDto> getRoles();

    @ApiModelProperty("邮箱")
    @Mapping
    String getEmail();

    @ApiModelProperty("手机号")
    @Mapping
    String getMobile();
}
