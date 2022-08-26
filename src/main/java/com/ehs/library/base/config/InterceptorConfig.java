package com.ehs.library.base.config;

import com.ehs.library.base.interceptor.LogInterceptor;
import com.ehs.library.base.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/bootstrap/css/**", "/*.ico", "/error");
//        registry.addInterceptor(new LoginCheckInterceptor())
//                .order(2)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/", "/member/login", "/member/signup", "/member/logout",
//                        "/bootstrap/css/**", "/*.ico", "/error"
//                );
    }
}
