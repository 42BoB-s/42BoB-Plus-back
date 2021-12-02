package com.bobsPlus.service;

import com.bobsPlus.dto.UserDto;

public interface LoginService {
//    String getOAuthToken(String code);
//    SessionDto getUserInfo(String token);
    void processNewUser(UserDto userDto);
//    HttpSession getSession(HttpServletRequest req, SessionDto sessionDto);
//    void destroySession(HttpSession httpSession);
}
