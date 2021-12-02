package com.bobsPlus.controller;

import com.bobsPlus.dto.UserDto;
import com.bobsPlus.service.ProfileImageService;
import com.bobsPlus.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class ProfileImageController {
    private final TokenService tokenService;

    @Value("${profile.image.dir}")
    private String imgDir;
    ProfileImageService imageService;

    @GetMapping("/images/{profile}")
    public Resource downloadImage(@PathVariable String profile) throws MalformedURLException {
        return new UrlResource("file:" + imgDir + profile);
    }

    @PostMapping("/mypage/picture")
    public ResponseEntity<HashMap<String, Object>> uploadProfile(@RequestParam MultipartFile file, HttpServletRequest req) {

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        UserDto userDto = tokenService.getToken(req);

        try {
            long result = imageService.uploadProfile(file, userDto.getId());
            resultMap.put("interCode", result);
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        } catch (IOException e) {
            long result = -15L;
            resultMap.put("interCode", result);
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        }
        return entity;
    }
}
