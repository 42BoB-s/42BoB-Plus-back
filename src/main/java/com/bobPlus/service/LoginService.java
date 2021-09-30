package com.bobPlus.service;

import com.bobPlus.dto.SessionDto;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface LoginService {
    String getOAuthToken(String code);
    SessionDto getUserInfo(String token);
    void processNewUser(SessionDto sessionDto);
    HttpSession getSession(HttpServletRequest req, SessionDto sessionDto);
    void destroySession(HttpSession httpSession);
}
