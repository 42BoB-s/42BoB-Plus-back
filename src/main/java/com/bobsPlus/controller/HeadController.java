package com.bobsPlus.controller;

import com.bobsPlus.dto.StatDto;
import com.bobsPlus.dto.UserDto;
import com.bobsPlus.entity.User;
import com.bobsPlus.repository.UserRepository;
import com.bobsPlus.service.TokenService;
import com.bobsPlus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class HeadController {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @GetMapping("/bobs/header")
    private ResponseEntity<HashMap<String, Object>> getMyHeader(HttpServletRequest req, HttpServletResponse resp)
    {
        UserDto userDto = tokenService.getToken(req);
        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        Optional<User> user = userRepository.findById(userDto.getId());
        if (user.isPresent()) {
            resultMap.put("interCode", 1);
            resultMap.put("user", user.get());
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        } else {
            resultMap.put("interCode", -20); // DB에 계정이 존재하지 않는 경우 (임시)
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        }
        return entity;
    }
}
