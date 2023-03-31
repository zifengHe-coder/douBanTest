package com.web.spirder.demo;

import com.google.common.base.Predicates;
import com.idaoben.web.common.dao.impl.BaseRepositoryImpl;
import com.web.spirder.demo.dao.entity.Admin;
import com.web.spirder.demo.dao.repository.AdminRepository;
import com.web.spirder.demo.security.AdminSecurityPrinciple;
import com.web.spirder.demo.security.DebuggingAuthFilter;
import com.web.spirder.demo.security.DebuggingAuthToken;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
@EnableJpaRepositories(basePackageClasses = {AdminRepository.class}, repositoryBaseClass = BaseRepositoryImpl.class, repositoryImplementationPostfix = "ExtImpl")
@EntityScan(basePackageClasses = {Admin.class})
@EnableScheduling
@MapperScan
public class PacongApplication {

    public static void main(String[] args) {
        SpringApplication.run(PacongApplication.class, args);
    }

    private ApiKey apiKey() {
        return new ApiKey("CurrentUser", DebuggingAuthFilter.DEBUGGING_CURRENT_USER_HEADER_NAME, "header");
    }

    @Bean
    public DebuggingAuthToken debuggingAuthToken() {
        return new DebuggingAuthToken();
    }

    @Bean
    public SecurityConfiguration securityConfiguration(DebuggingAuthToken authToken) {
        return new SecurityConfiguration(null, null, null, null,
                authToken.getToken(), ApiKeyVehicle.HEADER, DebuggingAuthFilter.DEBUGGING_ENABLE_DEBUG_TOKEN_HEADER_NAME,
                "");
    }

    @Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.or(
                        PathSelectors.ant("/api/**")))
                .build()
                .securitySchemes(Collections.singletonList(apiKey()))
                .ignoredParameterTypes(AdminSecurityPrinciple.class);
        return docket;
    }

}
