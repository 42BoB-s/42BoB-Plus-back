package com.example.projectprototype.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Value("${baseUrl}")
    private String baseUrl;


    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        // session 정보를 확인해서 없으면 login 으로 디라이렉션
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("session") == null) {
            resp.sendRedirect(baseUrl+"login");
            return false;
        }
        return true;
    }
}
