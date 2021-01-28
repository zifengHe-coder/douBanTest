package com.web.spirder.demo.web.application.system;

import com.idaoben.web.common.entity.Filter;
import com.idaoben.web.common.exception.ServiceException;
import com.idaoben.web.token_auth.application.AccessTokenApplicationService;
import com.web.spirder.demo.dao.entity.Admin;
import com.web.spirder.demo.dao.entity.Role;
import com.web.spirder.demo.dao.entity.utils.SnowflakeId;
import com.web.spirder.demo.exception.ErrorCode;
import com.web.spirder.demo.security.AdminSecurityPrinciple;
import com.web.spirder.demo.service.AdminService;
import com.web.spirder.demo.service.RoleService;
import com.web.spirder.demo.web.command.system.*;
import com.web.spirder.demo.web.dto.system.AdminDto;
import com.web.spirder.demo.web.utils.DtoTransformer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Component
public class AdminApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(AdminApplicationService.class);

    @Resource
    private AdminService adminService;
    @Resource
    private RoleService roleService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private AccessTokenApplicationService accessTokenApplicationService;
    @Resource
    private RoleApplicationService roleApplicationService;




    private static final String DEFAULT_PWD = "123456";



    @Transactional(readOnly = true)
    public AdminDto identity(AdminSecurityPrinciple principle){
       /* if(principle==null){
            throw new ServiceException(ErrorCode.PLZ_RE_LOGIN);
        }*/
        Admin admin = adminService.find(principle.getId());
        AdminDto adminDto = DtoTransformer.as(AdminDto.class).apply(admin).orElse(null);
        return adminDto;
    }

    @Transactional(readOnly = true)
    public AdminDto details(Long id){
        Admin admin = adminService.find(id);
        AdminDto adminDto = DtoTransformer.as(AdminDto.class).apply(admin).orElse(null);
        return adminDto;
    }

    @Transactional
    public void resetMyPassword(Long adminId, AdminPasswordResetCommand command){
        Admin admin = adminService.find(adminId);
        admin.setPassword(passwordEncoder.encode(command.getPassword()));
        adminService.save(admin);
    }

    @Transactional
    public void resetPassword(AdminPasswordOtherResetCommand command){
        Admin admin = adminService.find(command.getId());
        admin.setPassword(passwordEncoder.encode(command.getPassword()));
        adminService.save(admin);
    }


    @Transactional
    public void create(AdminCreateCommand command){
        if(adminService.exists(Filter.eq("username", command.getUsername()))){
            throw ServiceException.of(ErrorCode.ADMIN_USERNAME_EXIST);
        }

        Admin admin = new Admin();
        BeanUtils.copyProperties(command, admin, "password");
        if(command.getRoleIds() != null){
            for(Long roleId : command.getRoleIds()){
                admin.getRoles().add(roleService.find(roleId));
            }
        }
        admin.setPassword(passwordEncoder.encode(command.getPassword()));
        adminService.save(admin);
    }

    @Transactional
    public void update(AdminUpdateCommand command){
        Admin admin = adminService.find(command.getId());
        if(!admin.getUsername().equals(command.getUsername())){
            if(adminService.exists(Filter.eq("username", command.getUsername()))){
                throw ServiceException.of(ErrorCode.ADMIN_USERNAME_EXIST);
            }
        }
        BeanUtils.copyProperties(command, admin, "password");
        if(StringUtils.isNotEmpty(command.getPassword())){
            admin.setPassword(passwordEncoder.encode(command.getPassword()));
        }
        admin.getRoles().clear();
        if(command.getRoleIds() != null){
            for(Long roleId : command.getRoleIds()){
                admin.getRoles().add(roleService.find(roleId));
            }
        }
        adminService.save(admin);
    }

    @Transactional
    public void delete(AdminSecurityPrinciple principle,Long adminId){
        if(principle.getId().equals(adminId)){
            adminService.delete(adminId);
        }
    }

    @Transactional
    public void enable(AdminEnableCommand command){
        Admin admin = adminService.find(command.getId());
        admin.setEnabled(command.isEnable());
        adminService.save(admin);
    }

    @Transactional
    public void setRoles(AdminSetRolesCommand command){
        Admin admin = adminService.find(command.getId());
        if (admin == null) {
            throw ServiceException.of(ErrorCode.ADMIN_NOT_EXIST);
        }
        admin.getRoles().clear();
        if(command.getRoleIds() != null){
            for(Long roleId : command.getRoleIds()){
                admin.getRoles().add(roleService.find(roleId));
            }
        }
        adminService.save(admin);
    }




    @Transactional
    public boolean defaultOrganizationValue(){
        boolean result = false;
        //organization数据

        SnowflakeId snowflake = new SnowflakeId();

        Admin admin = adminService.findByProperty("username", "admin");
        if(admin == null){
/*			Organization org = new Organization();
			org.setName("未设置机构名称");
			org.setOrgType(OrgType.MEDICAL_INSTITUTION);
	        CodeNoVo codeNo = SequenceUtils.getLastSequenceNo(SequenceType.ORG_CODE_NO,TableNameConstant.T_ORGANIZATION);
	        org.setCodeNo(codeNo.getCodeNo());
			org = organizationService.saveAndFlush(org);*/


            //role，超级管理员数据
            Role role = roleService.findByProperty("roleName", "超级管理员");
            if(role == null){
                role = new Role();
                role.setRoleName("超级管理员");
                role.setDisplayOrder(1);
                role.setCodeNo("100001");
                // 角色权限,只初始化系统角色管理等
                HashSet<String> permissionList = new HashSet<>();
                permissionList.add("SYSTEM_MANAGE");
                permissionList.add("USER_SETTING");
                permissionList.add("USER_MANAGEMENT");
                permissionList.add("USER_MANAGEMENT_ADD");
                permissionList.add("USER_MANAGEMENT_EDIT");
                permissionList.add("ROLE_MANAGEMENT");
                permissionList.add("ROLE_MANAGEMENT_ADD");
                permissionList.add("ROLE_MANAGEMENT_EDIT");
                permissionList.add("ROLE_MANAGEMENT_AUTHORISE");
                role.setPermissions(permissionList);
                role = roleService.saveAndFlush(role);
            }

            //admin，admin账号数据
            //Admin admin = adminService.findByProperty("username", "admin");
            if (admin == null) {
                admin = new Admin();
                admin.setAdminFlag(true);
                admin.setUsername("admin");
                admin.setName("admin");
                admin.setDisplayOrder(1);
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.getRoles().add(roleService.find(role.getId()));
                adminService.save(admin);
            }

            result = true;
        }


        return result;
    }



    @Transactional(readOnly = true)
    public AdminDto defaultLogin(String username){
        Admin admin = adminService.findByUsername(username);
        AdminDto adminDto = DtoTransformer.as(AdminDto.class).apply(admin).orElse(null);

        AdminSecurityPrinciple principle = AdminSecurityPrinciple.fromAdmin(admin);
        SecurityContext context = SecurityContextHolder.getContext();
        PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(principle, "",
                principle.getAuthorities());
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);

        return adminDto;
    }

}