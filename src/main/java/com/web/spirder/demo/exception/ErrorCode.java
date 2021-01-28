package com.web.spirder.demo.exception;

public class ErrorCode {
    public static final String ADMIN_USERNAME_EXIST = "1006";
    /**
     * 管理员不存在
     */
    public static final String ADMIN_NOT_EXIST = "1007";
    /**
     * 角色已存在
     */
    public static final String ROLE_EXIST = "1009";
    /**
     * 编码已存在
     */
    public static final String CODE_EXIST = "1003";

    /**
     * 该角色不存在
     */
    public static final String ROLE_NOT_EXIST = "1008";
    /**
     * 该角色已有用户关联，需要移除用户角色后再删除
     */
    public static final String ROLE_HAS_ASSOCIATER_ADMIN = "1005";
}
