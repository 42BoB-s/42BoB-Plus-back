package com.bobsPlus.controller;

import com.bobsPlus.dto.UserDto;
import com.bobsPlus.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/token/error")
    public String auth() {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

       /*
        보안성 향상을 위해 refreshToken도 추후 필요
        jwt Token 형태 (헤더)
        -> Authorization : Bearer {jwt access token}
           ex) Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbiI6ImRvaGVsZWUiLCJlbWFpbCI6ImRvaGVsZWVAc3R1ZGVudC40MnNlb3VsLmtyIiwiaW1hZ2VfdXJsIjoiaHR0cHM6Ly9jZG4uaW50cmEuNDIuZnIvdXNlcnMvZG9oZWxlZS5qcGciLCJyb2xlIjoiVVNFUiIsInN1YiI6InVzZXItYXV0aCIsImlhdCI6MTYzNzE4NTM2OSwiZXhwIjoxNjM3MTg1OTY5fQ.luZiLo1SzSMkVX-2uAaNSaVUGhNEmu5mERPUzDSUDBM
     */

    //임시 토큰 발급
    @GetMapping("/bobs/header")
    public ResponseEntity<HashMap<String, String>> tmpToken()
    {
        ResponseEntity<HashMap<String, String>> entity;
        UserDto userDto = UserDto.builder()
                .id("testUser")
                .email("testUser@test.com")
                .profile("https://www.google.co.jp/logos/doodles/2021/seasonal-holidays-2021-6753651837109324.3-ladc.gif")
                .build();
        String token = tokenService.generateToken(userDto, "USER");
        HashMap<String, String> auth = new HashMap<>();
        auth.put("Authorization", token);
        entity = new ResponseEntity<>(auth, HttpStatus.FORBIDDEN);
        return entity;
    }

}