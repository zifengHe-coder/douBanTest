package com.web.spirder.demo.service;

import com.idaoben.web.common.service.BaseService;
import com.web.spirder.demo.dao.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<Role, Long> {
    /**
     * 获取全部权限定义
     * @return 权限定义树
     */
    List<Map<String, Object>> retrieveAllPermissions();
}
