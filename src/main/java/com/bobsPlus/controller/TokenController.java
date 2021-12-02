package com.bobsPlus.controller;

import com.bobsPlus.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
}