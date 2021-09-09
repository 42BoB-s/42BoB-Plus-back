package com.example.projectprototype.controller;


import com.example.projectprototype.dto.MealHistoryDto;
import com.example.projectprototype.dto.SessionDto;
import com.example.projectprototype.dto.StatDto;
import com.example.projectprototype.repository.UserRepository;
import com.example.projectprototype.service.RoomService;
import com.example.projectprototype.service.UserService;
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

    @GetMapping("/bobs/mypage/stat")
    private ResponseEntity<HashMap<String, Object>> searchMyStat(HttpServletRequest req, HttpServletResponse resp)
    {
        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null || !userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);
        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        StatDto stat = userRepository.searchStat(sessionDTO.getUserId()).get(0);
        resultMap.put("interCode", 1);
        resultMap.put("stat", stat);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    @GetMapping("/bobs/mypage/mylog")
    private ResponseEntity<HashMap<String, Object>> searchMyHistory()
    {
//        SessionDto sessionDTO = sessionCheck(req);
//        if (sessionDTO == null || !userService.userIdCheck(sessionDTO.getUserId()))
//            redirectLogin(resp);
        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        List<MealHistoryDto> mealHistoryDtoList= userRepository.searchHistory("mhong");
        resultMap.put("interCode", 1);
        resultMap.put("mealHistoryDtoList", mealHistoryDtoList);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    @GetMapping("bobs/mypage/ban")
    private ResponseEntity<HashMap<String, Object>> searchBanList(HttpServletRequest req, HttpServletResponse resp)
    {
        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null || !userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> banList = userService.searchBanList(sessionDTO.getUserId());
        resultMap.put("interCode", 1);
        resultMap.put("List<String>", banList);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    @PatchMapping("/bobs/mypage/ban/{userId}")
    private ResponseEntity<HashMap<String, Object>> addBan(HttpServletRequest req, HttpServletResponse resp, @PathVariable String userId)
    {
        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null || !userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        long result = userService.addBan(sessionDTO.getUserId(),userId);
        resultMap.put("interCode", (int) result);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    @DeleteMapping("/bobs/mypage/ban/{userId}")
    private ResponseEntity<HashMap<String, Object>> deleteBan(HttpServletRequest req, HttpServletResponse resp, @PathVariable String userId)
    {
        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null || !userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        long result = userService.deleteBan(sessionDTO.getUserId(),userId);
        resultMap.put("interCode", (int) result);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    private void redirectLogin(HttpServletResponse resp) {
        try {
            resp.sendRedirect(baseUrl + "login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SessionDto sessionCheck(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        return (SessionDto)session.getAttribute("session");
    }
}
