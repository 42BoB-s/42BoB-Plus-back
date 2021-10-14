package com.bobPlus.config;

import com.bobPlus.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    // {"/" , "/login" , "/logout", "/rooms" , "myroom", "/header"} 경로와 js,css,error 파일에서는 로그인 여부를 검사하지 않도록함.
    // 만약 myroom 까지 검사를 해버리면 메인 페이질 로딩중에 계속 login 으로 리다이랙션이 될것이기 때문임.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/logout", "/", "/42OAuth",
                        "/bobs/rooms", "/bobs/myroom", "/bobs/header",
                        "/css/**", "/*.ico", "/error");
    }
}
