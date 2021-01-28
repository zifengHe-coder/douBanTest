package com.web.spirder.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idaoben.web.common.api.bean.ApiResponse;
import com.web.spirder.demo.dao.entity.Admin;
import com.web.spirder.demo.dao.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * @author X
 */
public class AdminAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper;

    private AdminRepository adminRepository;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setAdminRepository(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 更新用户最后一次登录成功的时间
        AdminSecurityPrinciple principal = (AdminSecurityPrinciple) authentication.getPrincipal();

        ApiResponse<AdminSecurityPrinciple> apiResponse = ApiResponse.createSuccess(principal, request, response);
        String json = objectMapper.writeValueAsString(apiResponse);

        Admin admin = adminRepository.findByUsername(principal.getUsername());
        admin.setLastLoginTime(ZonedDateTime.now());
        adminRepository.save(admin);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getOutputStream().write(json.getBytes("UTF-8"));
        response.getOutputStream().flush();
    }
}
