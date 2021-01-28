package com.web.spirder.demo.dao.entity;

import com.idaoben.web.common.entity.Description;
import com.idaoben.web.common.entity.TrackableObject;
import com.web.spirder.demo.dao.entity.utils.TrackableObjectSnowFlake;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "t_role")
@NamedEntityGraph(name = "Role.detailed", attributeNodes = @NamedAttributeNode("permissions") )
@Description("角色")
public class Role extends TrackableObject {
    private static final long serialVersionUID = 399642783769711458L;

    /**
     * 角色名
     */
    @Column(name = "role_name")
    @Description("角色名")
    private String roleName;

    /**
     * 顺序号
     */
    @Column(name = "display_order")
    @Description("显示的顺序号，用于排序显示")
    private Integer displayOrder;

    /**
     * 编码
     */
    @Column(name = "code_no")
    @Description("编码")
    private String codeNo;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    @Description("备注")
    private String remark;

    /**
     * 是否启用
     */
    @Column(name = "enabled", nullable = false)
    @Description("是否启用")
    private boolean enabled = true;

    /**
     * 角色权限集
     */
    @ElementCollection
    @CollectionTable(name = "t_role_permission", joinColumns = @JoinColumn(name = "rp_role_id"))
    @Description("角色权限集")
    private Set<String> permissions = new HashSet<>();

    @Transient
    private Map<String, Boolean> permissionFlags = new HashMap<>();





    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 获取权限集
     *
     * @return permissions的值
     */
    public Set<String> getPermissions() {
        return permissions;
    }

    /**
     * 设置权限集
     *
     * @param permissions
     *            permissions的值
     */
    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
        for (String perm : permissions) {
            permissionFlags.put(perm, true);
        }
    }

    /**
     * 获取权限标识映射
     *
     * @return 权限标识映射
     */
    public Map<String, Boolean> getPermissionFlags() {
        return permissionFlags;
    }

    @PostLoad
    private void postLoad() {
        for (String perm : permissions) {
            permissionFlags.put(perm, true);
        }
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPermissionFlags(Map<String, Boolean> permissionFlags) {
        this.permissionFlags = permissionFlags;
    }

    public String getCodeNo() {
        return codeNo;
    }

    public void setCodeNo(String codeNo) {
        this.codeNo = codeNo;
    }
}
