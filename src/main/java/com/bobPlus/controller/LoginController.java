package com.bobPlus.controller;

import com.bobPlus.dto.SessionDto;
import com.bobPlus.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Value("${baseUrl}")
    private String baseUrl;

    @GetMapping("/42OAuth")
    public void OAuth(HttpServletRequest req, HttpServletResponse resp,
                      @RequestParam(value = "code") String code) {
        String token = loginService.getOAuthToken(code);
        SessionDto sessionDto = loginService.getUserInfo(token);
        loginService.processNewUser(sessionDto);
        //임시
        HttpSession httpSession = loginService.getSession(req, sessionDto);
        try {
            resp.sendRedirect(baseUrl + "main");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession)
    {
        if (httpSession != null)
            loginService.destroySession(httpSession);
        return "login";
    }
}
