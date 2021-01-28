package com.web.spirder.demo.service.impl;

import com.idaoben.web.common.service.impl.BaseServiceImpl;
import com.web.spirder.demo.dao.entity.Role;
import com.web.spirder.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 角色相关service
 * @author ryan
 *
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {

    private List<Map<String, Object>> permissions;

    /**
     * 权限定义文件
     */
    private org.springframework.core.io.Resource[] permissionDefinitions;

    /**
     * 设置权限定义文件
     * @param permissionDefinitions 权限定义文件
     */
    @Value("classpath*:/permissions.yaml")
    public void setPermissionDefinitions(org.springframework.core.io.Resource[] permissionDefinitions) {
        this.permissionDefinitions = permissionDefinitions;
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        YamlMapFactoryBean yamlMapFactoryBean = new YamlMapFactoryBean();
        yamlMapFactoryBean.setResources(permissionDefinitions);
        yamlMapFactoryBean.afterPropertiesSet();
        Map<String, Object> map = yamlMapFactoryBean.getObject();
        permissions = (List<Map<String, Object>>) map.get("document");
    }

    /**
     * 获取全部权限定义
     *
     * @return 权限定义树
     */
    @Override
    public List<Map<String, Object>> retrieveAllPermissions() {
        return permissions;
    }

    @Override
    public Role save(Role entity) {
        return super.save(entity);
    }

    @Override
    public Iterable<Role> save(Iterable<Role> entities) {
        return super.save(entities);
    }
}
