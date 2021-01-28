package com.web.spirder.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DebuggingAuthFilter extends OncePerRequestFilter {
    /**
     * 调试模式 - 当前用户使用的HEADER名称
     */
    public static final String DEBUGGING_CURRENT_USER_HEADER_NAME = "X-DBD-CURRENT-USER";

    /**
     * 调试模式 - 启用调试模式的HEADER名称
     */
    public static final String DEBUGGING_ENABLE_DEBUG_TOKEN_HEADER_NAME = "X-DBD-ENABLE-DEBUG-TOKEN";

    @Value("${auth.auth-debug-enabled}")
    private boolean authDebugEnabled;

    @Resource(name="adminUserDetailsService")
    private UserDetailsService adminUserDetailsService;

    @Resource
    private DebuggingAuthToken token;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 如果调试模式未启用，跳过
        if (!authDebugEnabled) {
            filterChain.doFilter(request, response);
            return;
        }

        // 凡是带有调试用的HEADER的，都使用HEADER中携带的用户ID信息。如果ID不存在，抛出异常
        String currentUserHeader = request.getHeader(DEBUGGING_CURRENT_USER_HEADER_NAME);
        String tokenHeader = request.getHeader(DEBUGGING_ENABLE_DEBUG_TOKEN_HEADER_NAME);

        if (currentUserHeader == null || tokenHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails user = null;
        try {
            user = adminUserDetailsService.loadUserByUsername(currentUserHeader);
        }catch (UsernameNotFoundException e){
            logger.error(e.getMessage(), e);
        }

        if (user == null) {
            throw new UsernameNotFoundException(String.format("无法找到用户 '%s'",
                    currentUserHeader));
        }

        if (!token.getToken().equals(tokenHeader)) {
            throw new AuthenticationServiceException("Token不匹配");
        }

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(user, "", user.getAuthorities());
        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);

        filterChain.doFilter(request, response);
    }
}
