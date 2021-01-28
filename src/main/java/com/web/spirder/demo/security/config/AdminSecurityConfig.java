package com.web.spirder.demo.security.config;

import com.idaoben.web.common.security.*;
import com.web.spirder.demo.security.AdminAuthenticationSuccessHandler;
import com.web.spirder.demo.security.AdminUserDetailService;
import com.web.spirder.demo.security.DebuggingAuthFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author X
 */
@Configuration
@Order(99)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${daoben.framework.captcha-disable}")
    private boolean captchaDisable;


    @Bean
    public AdminUserDetailService adminUserDetailsService() {
        AdminUserDetailService adminUserDetailsService = new AdminUserDetailService();
        return adminUserDetailsService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AdminAuthenticationSuccessHandler adminAuthenticationSuccessHandler() throws Exception {
        return new AdminAuthenticationSuccessHandler();
    }

    @Bean
    public DefaultAuthenticationFailureHandler defaultAuthenticationFailureHandler() throws Exception {
        return new DefaultAuthenticationFailureHandler();
    }

    @Bean
    public DefaultLogoutSuccessHandler defaultLogoutSuccessHandler() throws Exception {
        return new DefaultLogoutSuccessHandler();
    }


    @Bean
    public CaptchaCodeFilter captchaCodeFilter(@Qualifier("defaultAuthenticationFailureHandler") DefaultAuthenticationFailureHandler adminAuthenticationFailureHandler) {
        CaptchaCodeFilter captchaCodeFilter = new CaptchaCodeFilter("/api/admin/login");
        captchaCodeFilter.setFailureHandler(adminAuthenticationFailureHandler);
        return captchaCodeFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminUserDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.mvcMatcher("/**").authorizeRequests()
                .mvcMatchers("/api/**").permitAll() //先全部授权，后续再根据情况调整
                .and()
                .formLogin().loginProcessingUrl("/api/admin/login")
                .successHandler(adminAuthenticationSuccessHandler())
                .failureHandler(defaultAuthenticationFailureHandler())
                .and()
                //暂时取消验证码
                //.addFilterBefore(captchaCodeFilter(defaultAuthenticationFailureHandler()), UsernamePasswordAuthenticationFilter.class)
                .logout().logoutUrl("/api/admin/logout").logoutSuccessHandler(defaultLogoutSuccessHandler())
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(loginRequiredJsonAuthenticationEntryPoint())
                .accessDeniedHandler(defaultAccessDeniedHandler())
                .and()
                .addFilterBefore(debuggingAuthFilter(), AnonymousAuthenticationFilter.class).cors().and()//临时加上跨域;
                .headers().frameOptions().sameOrigin();
        if (!captchaDisable) {
            http.addFilterBefore(captchaCodeFilter(defaultAuthenticationFailureHandler()), UsernamePasswordAuthenticationFilter.class);
        }
    }

    @Bean
    public DebuggingAuthFilter debuggingAuthFilter() throws Exception {
        return new DebuggingAuthFilter();
    }

    @Bean
    public DefaultAccessDeniedHandler defaultAccessDeniedHandler() throws Exception {
        return new DefaultAccessDeniedHandler();
    }

    @Bean
    public LoginRequiredJsonAuthenticationEntryPoint loginRequiredJsonAuthenticationEntryPoint() throws Exception {
        return new LoginRequiredJsonAuthenticationEntryPoint();
    }
}