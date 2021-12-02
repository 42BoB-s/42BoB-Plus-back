package com.bobsPlus.config;

import com.bobsPlus.dto.UserDto;
import com.bobsPlus.service.TokenService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {
    private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorization = ((HttpServletRequest)request).getHeader("Authorization");
        // Authorized Bearer
        if (authorization != null)
        {
            if (Pattern.matches("^Bearer .*", authorization)) {
                String token = authorization.replaceAll("^Bearer( )*", "");
                //토근의 존재 유무, 토큰 유효성 체크, 토큰 유효기간 체크
                if (tokenService.verifyToken(token)) {
                    UserDto userDto = UserDto.builder()
                            .id(tokenService.getId(token))
                            .email(tokenService.getEmail(token))
                            .profile(tokenService.getImageUrl(token))
                            .build();

                    Authentication auth = getAuthentication(userDto);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        chain.doFilter(request, response);
    }

    public Authentication getAuthentication(UserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}