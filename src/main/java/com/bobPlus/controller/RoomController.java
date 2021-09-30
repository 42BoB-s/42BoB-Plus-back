package com.bobPlus.controller;

import com.bobPlus.dto.RoomDto;
import com.bobPlus.dto.SearchRoomsRequestDto;
import com.bobPlus.dto.SessionDto;
import com.bobPlus.dto.UpdateTitleRequestDto;
import com.bobPlus.service.RoomService;
import com.bobPlus.service.UserService;
import com.example.projectprototype.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
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
    @GetMapping("/room")
    public ResponseEntity<HashMap<String, Object>> createRoom(HttpServletRequest req, RoomDto roomDTO) {

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        SessionDto sessionDTO = (SessionDto) req.getSession(false).getAttribute("session");

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
    public ResponseEntity<HashMap<String, Object>> searchMyRoom(HttpServletRequest req) {

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("session") == null) {
            resultMap.put("interCode", 1);
            resultMap.put("List<RoomDto>", null);
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
            return entity;
        }

        SessionDto sessionDTO = (SessionDto) session.getAttribute("session");
        List<RoomDto> roomDtoList = roomService.searchMyRooms(sessionDTO.getUserId());

        resultMap.put("interCode", 1);
        resultMap.put("List<RoomDto>", roomDtoList);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    // 필터링된 방 조회
    @GetMapping("/rooms")
    public ResponseEntity<HashMap<String, Object>> searchRooms(HttpServletRequest req,
                                        Pageable pageable, SearchRoomsRequestDto reqDto) {

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();

        long check = roomService.paramsCheck(reqDto);
        if (check < 0L) {
            resultMap.put("interCode", (int) check);
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
            return entity;
        }

        List<RoomDto> roomDtoList;
        HttpSession session = req.getSession(false);
        if (req.getSession(false) == null || session.getAttribute("session") == null) {
            roomDtoList = roomService.searchRooms(reqDto, pageable);
        }
        else {
            SessionDto sessionDTO = (SessionDto) session.getAttribute("session");
            roomDtoList = roomService.searchRooms(sessionDTO.getUserId(), reqDto, pageable);
        }

        resultMap.put("interCode", 1);
        resultMap.put("List<RoomDto>", roomDtoList);
        entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        return entity;
    }

    @PatchMapping("/room/enter/{roomid}")
    public ResponseEntity<HashMap<String, Object>> enterRoom(HttpServletRequest req,
                                       @PathVariable String roomid){

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        SessionDto sessionDTO = (SessionDto) req.getSession(false).getAttribute("session");

        long result = roomService.enterRoom(sessionDTO.getUserId(), roomid);
        if (result < 0L)
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        else
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        resultMap.put("interCode", (int) result);
        return entity;
    }

    @DeleteMapping("/room/exit/{roomid}")
    public ResponseEntity<HashMap<String, Object>> exitRoom(HttpServletRequest req,
                                @PathVariable String roomid) {

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        SessionDto sessionDTO = (SessionDto) req.getSession(false).getAttribute("session");

        long result = roomService.exitRoom(sessionDTO.getUserId(), roomid);
        if (result < 0L)
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        else
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        resultMap.put("interCode", (int) result);
        return entity;
    }

    @PutMapping("room/title/{roomid}")
    public ResponseEntity<HashMap<String, Object>> updateTitle(HttpServletRequest req,
                                                                @RequestBody UpdateTitleRequestDto titleDto,
                                                                @PathVariable String roomid) {

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();
        SessionDto sessionDTO = (SessionDto) req.getSession(false).getAttribute("session");

        long result = roomService.updateTitle(titleDto.getTitle(), roomid, sessionDTO.getUserId());

        if (result < 0L)
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        else
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        resultMap.put("interCode", (int) result);
        return entity;
    }


}