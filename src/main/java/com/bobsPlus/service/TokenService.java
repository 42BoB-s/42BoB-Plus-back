package com.bobsPlus.service;

import com.bobsPlus.dto.TokenDto;
import com.bobsPlus.dto.UserDto;
import com.bobsPlus.dto.UserDto;
import com.bobsPlus.entity.Token;
import com.bobsPlus.mapper.TokenMapper;
import com.bobsPlus.repository.TokenRepository;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;
    private String secretKey = "bPdSgVkYp3s6v9y$B&E)H@McQfTjWmZq";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //String uid만 하면 id만 들어감 email, image_url도 포함 시키도록 수정
    public TokenDto generateToken(UserDto userDto, String role) {
        //long accessTokenPeriod = 1000L * 30L ; //30초
        long accessTokenPeriod = 1000L * 60L * 60L * 24L; // 1일
        //long refreshTokenPeriod = 1000L * 60L; // 1분
        long refreshTokenPeriod = 1000L * 60L * 60L * 24L * 30; // 30일

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
        TokenDto tokenDto = new TokenDto();
        // tokens[0] : accessToken
        tokenDto.setAccessToken("Bearer " + Jwts.builder()
                .setHeader(headers)
                .setClaims(claims)
                .setSubject("user-auth")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact());
        // tokens[1] : refreshToken
        tokenDto.setRefreshToken("Bearer " + Jwts.builder()
                .setHeader(headers)
                .setClaims(claims)
                .setSubject("user-auth")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact());
        return tokenDto;
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

    public void saveRefreshToken(TokenDto tokenDto)
    {
        tokenDto.setId(tokenRepository.count());
        tokenRepository.save(tokenMapper.toEntity(tokenDto));
    }

    public String NewAccessTokenToken(String accessToken)
    {
        Optional<Token> token = tokenRepository.findByAccessToken(accessToken);
        if (token.isPresent()) {
            String refreshToken = token.get().getRefreshToken().replaceAll("^Bearer( )*", "");;
            if (verifyToken(refreshToken)) {
                UserDto userDto = UserDto.builder()
                        .id(getId(refreshToken))
                        .email(getEmail(refreshToken))
                        .profile(getImageUrl(refreshToken))
                        .build();
                TokenDto tokenDto = generateToken(userDto, "USER");
                tokenDto.setRefreshToken("Bearer " + refreshToken);
                //accessToken db저장 (이러면 발급할때마다 저장해줘야하는거 아니야...?)
                saveRefreshToken(tokenDto); // 저장이 아니라 기존 리프레쉬 토큰 행에 업데이트 해주는 편이 데이터가 쌓였을때 좋을꺼 같음
                return tokenDto.getAccessToken();
            }
        }
        return null;
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