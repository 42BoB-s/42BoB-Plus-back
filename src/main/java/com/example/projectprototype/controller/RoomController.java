package com.example.projectprototype.controller;

import com.example.projectprototype.dto.*;
import com.example.projectprototype.service.RoomService;
import com.example.projectprototype.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/bobs")
@RequiredArgsConstructor
public class RoomController {

    @Value("${baseUrl}")
    private String baseUrl;

    private final UserService userService;
    private final RoomService roomService;

    // 방 생성
    @PostMapping("/room")
    public ResponseEntity<HashMap<String, Object>> createRoom(RoomDto roomDTO,
                                                              HttpServletRequest req, HttpServletResponse resp) {
        // session 값이 있는지 확인, 없으면 login 화면으로 보내기.
        // session 값이 있으면, 주어진 session 값에 id가 등록된 회원인지 확인
        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null || !userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();

        long result = roomService.createRoom(roomDTO, sessionDTO.getUserId());

        if (result < 0L) {
            resultMap.put("interCode", (int) result);
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        } else {
            resultMap.put("interCode", 1);
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
        return entity;
    }

    // 예약한 방 조회
    @GetMapping("/myroom")
    public ResponseEntity<HashMap<String, Object>> searchMyRoom(HttpServletRequest req, HttpServletResponse resp) {

        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null || !userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();

        List<RoomDto> roomDtoList = roomService.searchMyRooms(sessionDTO.getUserId());

        resultMap.put("interCode", 1);
        resultMap.put("List<RoomDto>", roomDtoList);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    // 필터링된 방 조회
    @GetMapping("/rooms")
    public ResponseEntity<HashMap<String, Object>> searchRooms(HttpServletRequest req, HttpServletResponse resp,
                                        Pageable pageable, SearchRoomsRequestDto reqDto,
                                                               @RequestParam String userId) {

        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null || !userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();

        long check = roomService.paramsCheck(reqDto);
        if (check < 0L) {
            resultMap.put("interCode", (int) check);
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
            return entity;
        }

        List<RoomDto> roomDtoList = roomService.searchRooms(userId,reqDto, pageable);
        resultMap.put("interCode", 1);
        resultMap.put("List<RoomDto>", roomDtoList);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    @PatchMapping("/room/{roomid}")
    public ResponseEntity<HashMap<String, Object>> enterRoom(HttpServletRequest req, HttpServletResponse resp,
                                       @PathVariable String roomid){

        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null || !userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();

        long result = roomService.enterRoom(sessionDTO.getUserId(), roomid);
        if (result < 0L)
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        else
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        resultMap.put("interCode", (int) result);
        return entity;
    }

    @DeleteMapping("/room/{roomid}")
    public ResponseEntity<HashMap<String, Object>> exitRoom(HttpServletRequest req, HttpServletResponse resp,
                                @PathVariable String roomid) {

        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null || !userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();

        long result = roomService.exitRoom(sessionDTO.getUserId(), roomid);
        if (result < 0L)
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        else
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        resultMap.put("interCode", (int) result);
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
        SessionDto sessionDTO = (SessionDto)session.getAttribute("session");
        if (sessionDTO == null) {
            return null;
        }
        return sessionDTO;
    }
}
