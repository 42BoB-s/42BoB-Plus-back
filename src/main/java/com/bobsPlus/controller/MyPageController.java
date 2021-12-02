package com.bobsPlus.controller;

import com.bobsPlus.dto.UserDto;
import com.bobsPlus.service.TokenService;
import com.bobsPlus.service.UserService;
import com.bobsPlus.dto.MealHistoryDto;
import com.bobsPlus.dto.StatDto;
import com.bobsPlus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    @Value("${baseUrl}")
    private String baseUrl;

    private final UserService userService;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @GetMapping("/bobs/mypage/stat")
    private ResponseEntity<HashMap<String, Object>> searchMyStat(HttpServletRequest req, HttpServletResponse resp)
    {
        UserDto userDto = tokenService.getToken(req);
        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        StatDto stat = userRepository.searchStat(userDto.getId()).get(0);
        resultMap.put("interCode", 1);
        resultMap.put("stat", stat);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    @GetMapping("/bobs/mypage/mylog")
    private ResponseEntity<HashMap<String, Object>> searchMyHistory(HttpServletRequest req, HttpServletResponse resp)
    {
        UserDto userDto = tokenService.getToken(req);
        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        List<MealHistoryDto> mealHistoryDtoList= userRepository.searchHistory(userDto.getId());
        resultMap.put("interCode", 1);
        resultMap.put("mealHistoryDtoList", mealHistoryDtoList);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    @GetMapping("bobs/mypage/ban")
    private ResponseEntity<HashMap<String, Object>> searchBanList(HttpServletRequest req, HttpServletResponse resp)
    {
        UserDto userDto = tokenService.getToken(req);
        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> banList = userService.searchBanList(userDto.getId());
        resultMap.put("interCode", 1);
        resultMap.put("List<String>", banList);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    @PatchMapping("/bobs/mypage/ban/{userId}")
    private ResponseEntity<HashMap<String, Object>> addBan(HttpServletRequest req, HttpServletResponse resp, @PathVariable String userId)
    {
        UserDto userDto = tokenService.getToken(req);
        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        long result = userService.addBan(userDto.getId(),userId);
        resultMap.put("interCode", (int) result);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    @DeleteMapping("/bobs/mypage/ban/{userId}")
    private ResponseEntity<HashMap<String, Object>> deleteBan(HttpServletRequest req, HttpServletResponse resp, @PathVariable String userId)
    {
        UserDto userDto = tokenService.getToken(req);
        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        long result = userService.deleteBan(userDto.getId(),userId);
        resultMap.put("interCode", (int) result);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

}
