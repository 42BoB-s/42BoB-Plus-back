package com.bobsPlus.config;

import com.bobsPlus.dto.TokenDto;
import com.bobsPlus.dto.UserDto;
import com.bobsPlus.dto.UserRequestMapper;
import com.bobsPlus.mapper.UserMapper;
import com.bobsPlus.repository.TokenRepository;
import com.bobsPlus.repository.UserRepository;
import com.bobsPlus.service.LoginService;
import com.bobsPlus.service.TokenService;
import com.bobsPlus.service.UserService;
import com.bobsPlus.dto.UserDto;
import com.bobsPlus.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final UserRequestMapper userRequestMapper;
    private final ObjectMapper objectMapper;
    private final LoginService loginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        UserDto userDto = userRequestMapper.toDto(oAuth2User);

        // 로그인 시 토큰 발행
        TokenDto tokenDto = tokenService.generateToken(userDto, "USER");
        log.info("{}", tokenDto);

        // userId 가 DB 에 없다면 user 정보를 등록 (에러로 인해 임시 주석)
        loginService.processNewUser(userDto);

        //User DB에 refreshToken 저장필요 (accessToken는 무결성 검사용)
        tokenService.saveRefreshToken(tokenDto);

        //토큰 전달
        //response.setContentType("application/json;charset=UTF-8");
        //response.addHeader("Authorization", token);

        //커스텀 헤더를 리다이렉트로 전달하는 것은 불가.
        //일단 get 파라미터로 넘기도록 하였음.
        getRedirectStrategy().sendRedirect(request, response, "https://42bobs.netlify.app?Authorization="+ tokenDto.getAccessToken());
    }
}