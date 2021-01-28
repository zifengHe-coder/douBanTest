package com.web.spirder.demo.web.application.system;

import com.idaoben.web.common.entity.Filter;
import com.idaoben.web.common.exception.ServiceException;
import com.web.spirder.demo.dao.entity.Role;
import com.web.spirder.demo.exception.ErrorCode;
import com.web.spirder.demo.service.AdminService;
import com.web.spirder.demo.service.RoleService;
import com.web.spirder.demo.web.command.EnableCommand;
import com.web.spirder.demo.web.command.IdCommand;
import com.web.spirder.demo.web.command.IdsCommand;
import com.web.spirder.demo.web.command.system.RoleCreateCommand;
import com.web.spirder.demo.web.command.system.RoleListCommand;
import com.web.spirder.demo.web.command.system.RoleUpdateCommand;
import com.web.spirder.demo.web.command.system.SetRolePermissionsCommand;
import com.web.spirder.demo.web.dto.system.RoleDto;
import com.web.spirder.demo.web.utils.DtoTransformer;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class RoleApplicationService {

    @Resource
    private RoleService roleService;

    @Resource
    private AdminService adminService;



    @Transactional(readOnly = true)
    public Page<RoleDto> list(RoleListCommand command, Pageable pageable){
        Page<Role> roles = roleService.findPage(Filter.createFilters(Filter.multiSearch("codeNo|roleName", command.getKeyword(), Role.class),Filter.eq("enabled", command.getEnabled())),pageable);
        return DtoTransformer.asPage(RoleDto.class).apply(roles);
    }

    @Transactional
    public void create(RoleCreateCommand command){
        if(roleService.exists(Filter.eq("roleName", command.getRoleName()))){
            throw ServiceException.of(ErrorCode.ROLE_EXIST);
        }
        //检验编码
        if(roleService.exists(Filter.eq("codeNo", command.getCodeNo()))){
            throw ServiceException.of(ErrorCode.CODE_EXIST);
        }
        Role role = new Role();
        BeanUtils.copyProperties(command, role);
        roleService.save(role);
    }

    @Transactional
    public void update(RoleUpdateCommand command){
        Role role = roleService.find(command.getId());
        if(role == null){
            throw ServiceException.of(ErrorCode.ROLE_NOT_EXIST);
        }
        if(roleService.exists(Filter.eq("roleName", command.getRoleName())) && !command.getRoleName().equals(role.getRoleName()) ){
            throw ServiceException.of(ErrorCode.ROLE_EXIST);
        }
        //检验编码
        if(roleService.exists(Filter.eq("codeNo", command.getCodeNo())) && !command.getCodeNo().equals(role.getCodeNo())){
            throw ServiceException.of(ErrorCode.CODE_EXIST);
        }
        BeanUtils.copyProperties(command, role);
        roleService.save(role);
    }

    @Transactional(readOnly = true)
    public RoleDto detail(IdCommand command){
        Role role = roleService.find(command.getId());
        return DtoTransformer.as(RoleDto.class).apply(role).orElse(null);
    }

    @Transactional
    public void delete(IdsCommand command) {
        List<Long> ids = command.getIds();
        for (Long id : ids) {
            int number = adminService.findByRole(roleService.find(id));
            if (number > 0) {
                throw ServiceException.of(ErrorCode.ROLE_HAS_ASSOCIATER_ADMIN);
            }
            roleService.delete(id);
        }
    }


    public List<Map<String, Object>> listPermissions(){
        return roleService.retrieveAllPermissions();
    }



    @Transactional
    public void setRolePermissions(SetRolePermissionsCommand command){
        Role role = roleService.find(command.getId());
        if(role == null){
            throw ServiceException.of(ErrorCode.ROLE_NOT_EXIST);
        }
        role.getPermissions().clear();
        if(command.getPermissions() != null){
            role.setPermissions(command.getPermissions());
        }
        roleService.save(role);
    }

    @Transactional
    public void enable(EnableCommand command){
        Role role = roleService.find(command.getId());
        role.setEnabled(command.isEnabled());
        roleService.save(role);
    }

}
