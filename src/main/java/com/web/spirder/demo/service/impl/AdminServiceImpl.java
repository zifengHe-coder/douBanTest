package com.web.spirder.demo.service.impl;

import com.idaoben.web.common.service.impl.BaseServiceImpl;
import com.web.spirder.demo.dao.entity.Admin;
import com.web.spirder.demo.dao.entity.Role;
import com.web.spirder.demo.dao.repository.AdminRepository;
import com.web.spirder.demo.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin,Long> implements AdminService {
    @Resource
    private AdminRepository adminRepository;

    @Override
    public Admin save(Admin entity) {
        return super.save(entity);
    }

    @Override
    public Iterable<Admin> save(Iterable<Admin> entities) {
        return super.save(entities);
    }

    @Override
    public int findByRole(Role role) {
        return adminRepository.findByRole(role).size();
    }

    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsernameWithRole(username);
    }

    @Override
    public void delete(Long aLong) {
        super.delete(aLong);
    }
}
