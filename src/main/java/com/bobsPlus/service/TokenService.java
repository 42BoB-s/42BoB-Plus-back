package com.bobsPlus.service;

import com.bobsPlus.dto.UserDto;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class TokenService {

    @Value("${jwtSecret}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //String uid만 하면 id만 들어감 email, image_url도 포함 시키도록 수정
    public String generateToken(UserDto userDto, String role) {
        long tokenPeriod = 1000L * 60L * 60L * 24L; // 1일

        //Header
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //Claims(=payload)
        Claims claims = Jwts.claims();
        claims.put("login", userDto.getId());
        claims.put("email", userDto.getEmail());
        claims.put("image_url", userDto.getProfile());
        claims.put("role", role);

        Date now = new Date();
        return "Bearer " + Jwts.builder()
                            .setHeader(headers)
                            .setClaims(claims)
                            .setSubject("user-auth")
                            .setIssuedAt(now)
                            .setExpiration(new Date(now.getTime() + tokenPeriod))
                            .signWith(SignatureAlgorithm.HS256, secretKey)
                            .compact();
    }

    //컨트롤러에서 이용할 예정
    public UserDto getToken(HttpServletRequest request)
    {
        String authorization = request.getHeader("Authorization");
        // Authorized Bearer
        if (authorization != null)
        {
            if (Pattern.matches("^Bearer .*", authorization)) {
                String token = authorization.replaceAll("^Bearer( )*", "");
                //토근의 존재 유무, 토큰 유효성 체크, 토큰 유효기간 체크
                if (verifyToken(token)) {
                    UserDto userDto = UserDto.builder()
                            .id(getId(token))
                            .email(getEmail(token))
                            .profile(getImageUrl(token))
                            .build();
                    return (userDto);
                }
            }
        }
        return null;
    }

    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (ExpiredJwtException e) {
            System.out.println("# =====Jwt Token Expired=====");
            return false;
        } catch (Exception e) {
            System.out.println("# =====Error=====");
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public String getId(String token)
    {
        return getClaims(token).get("login", String.class);
    }

    public String getEmail(String token)
    {
        return getClaims(token).get("email", String.class);
    }

    public String getImageUrl(String token)
    {
        return getClaims(token).get("image_url", String.class);
    }

}