package com.ehs.library.base.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("LoginCheckInterceptor run {}", requestURI);
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginMember") == null) {
            log.info("unlogin user request");
            //로그인으로 redirect
            response.sendRedirect("/member/login?redirectURL=" + requestURI);
            return false;
        }
        return true;
    }
}