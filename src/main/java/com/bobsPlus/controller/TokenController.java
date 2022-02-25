package com.bobsPlus.controller;

import com.bobsPlus.dto.TokenDto;
import com.bobsPlus.dto.UserDto;
import com.bobsPlus.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

//    @GetMapping("/token/error")
//    public String auth() {
//        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//    }
//
//       /*
//        보안성 향상을 위해 refreshToken도 추후 필요
//        jwt Token 형태 (헤더)
//        -> Authorization : Bearer {jwt access token}
//           ex) Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbiI6ImRvaGVsZWUiLCJlbWFpbCI6ImRvaGVsZWVAc3R1ZGVudC40MnNlb3VsLmtyIiwiaW1hZ2VfdXJsIjoiaHR0cHM6Ly9jZG4uaW50cmEuNDIuZnIvdXNlcnMvZG9oZWxlZS5qcGciLCJyb2xlIjoiVVNFUiIsInN1YiI6InVzZXItYXV0aCIsImlhdCI6MTYzNzE4NTM2OSwiZXhwIjoxNjM3MTg1OTY5fQ.luZiLo1SzSMkVX-2uAaNSaVUGhNEmu5mERPUzDSUDBM
//     */

    //accessToken 토큰 재발급
    @GetMapping("/token/refresh")
    public ResponseEntity<HashMap<String, Object>> refreshToken(HttpServletRequest req)
    {
        String accessToken = ((HttpServletRequest)req).getHeader("Authorization");
        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        if (accessToken != null) {
            String newAccessToken = tokenService.NewAccessTokenToken(accessToken);
            if (newAccessToken != null) { // refresh 토큰이 만료되지 않은 경우 (재발급 가능)
                resultMap.put("interCode", 1);
                resultMap.put("Authorization", newAccessToken);
                entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
            } else { // refresh 토큰까지 만료된 경우 (재발급 불가) 또는 토큰이 잘못된 경우
                resultMap.put("interCode", -16);
                entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
            }
        }
        else { // Authorization 헤더 없음
            resultMap.put("interCode", -17);
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        }
        return entity;
    }

    //임시 토큰 발급
    @GetMapping("/token/tmp")
    public ResponseEntity<HashMap<String, String>> tmpToken()
    {
        ResponseEntity<HashMap<String, String>> entity;
        UserDto userDto = UserDto.builder()
                .id("dev")
                .email("dev@student.42seoul.kr")
                .profile("https://profile.intra.42.fr/assets/42_logo_black-684989d43d629b3c0ff6fd7e1157ee04db9bb7a73fba8ec4e01543d650a1c607.png")
                .build();
        TokenDto tokenDto = tokenService.generateToken(userDto, "USER");
        HashMap<String, String> auth = new HashMap<>();
        auth.put("Authorization", tokenDto.getAccessToken());
        //리프레쉬도 db에 입력해줘야됨;; 임시로 만든것도 사용할수 잇게
        tokenService.saveRefreshToken(tokenDto);
        entity = new ResponseEntity<>(auth, HttpStatus.OK);
        return entity;
    }

}