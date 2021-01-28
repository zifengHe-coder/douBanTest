package com.web.spirder.demo.security;

import com.web.spirder.demo.dao.entity.Admin;
import com.web.spirder.demo.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class AdminUserDetailService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(AdminUserDetailService.class);
    @Resource
    private DebuggingAuthToken token;
    @Resource
    private AdminService adminService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        Admin admin = adminService.findByUsername(loginName);
        if (admin == null) {
            throw new UsernameNotFoundException("找不到用户 " + loginName);
        }
        logger.info("登陆成功token:{}",token.getToken());
        return AdminSecurityPrinciple.fromAdmin(admin);
    }
}
