package com.web.spirder.demo.service;

import com.idaoben.web.common.service.BaseService;
import com.web.spirder.demo.dao.entity.Admin;
import com.web.spirder.demo.dao.entity.Role;

public interface AdminService extends BaseService<Admin, Long>  {
    int findByRole(Role role);

    Admin findByUsername(String username);

}
