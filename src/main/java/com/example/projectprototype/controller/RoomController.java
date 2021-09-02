package com.example.projectprototype.controller;

import com.example.projectprototype.dto.*;
import com.example.projectprototype.service.RoomService;
import com.example.projectprototype.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {

    @Value("${baseUrl}")
    private String baseUrl;

    private final UserService userService;
    private final RoomService roomService;

    // 방 생성
    @PostMapping("/bobs/room")
    public ResponseDto createRoom(RoomDto roomDTO,
                                  HttpServletRequest req, HttpServletResponse resp) {
        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null)
            redirectLogin(resp);

        if (!userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        long result = roomService.createRoom(roomDTO, sessionDTO.getUserId());

        ResponseDto responseDTO = new ResponseDto();
        responseDTO.setCode(200);
        responseDTO.setInterCode((int)result);
        return responseDTO;
    }

    // 예약한 방 조회
    @GetMapping("/bpbs/myroom")
    public ListDto<RoomDto> searchMyRoom(HttpServletRequest req, HttpServletResponse resp) {

        // session 값이 있는지 확인, 없으면 login 화면으로 보내기.
        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null)
            redirectLogin(resp);

        // session 값이 있으면, 주어진 session 값에 id가 등록된 회원인지 확인
        if (!userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        // 주어진 회원 정보가 존재하는 회원이면 해당 회원이 속해있는 방 정보 전달
        return roomService.searchMyRooms(sessionDTO.getUserId());
    }

    // 필터링된 방 조회
    // menu 는 List로 받으면 List로 받아짐.
    // response 받는걸 dto 로 통일하기.
    @GetMapping("/bobs/rooms")
    public ListDto<RoomDto> searchRooms(HttpServletRequest req, HttpServletResponse resp, Pageable pageable,
                               @RequestParam String location,
                               @RequestParam String menu,
                               @RequestParam String startTime,
                               @RequestParam String endTime,
                               @RequestParam String keyword) {

        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null)
            redirectLogin(resp);

        if (!userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        ListDto<RoomDto> responseDto = new ListDto<>();
        long check = roomService.paramsCheck(location, menu, startTime, endTime);
        if (check < 0L) {
            responseDto.setCode(500);
            responseDto.setInterCode((int)check);
            return responseDto;
        }

        List<RoomDto> roomDtoList = roomService.searchRooms(sessionDTO.getUserId(),
                location, menu, startTime, endTime, keyword, pageable);
        responseDto.setComponent(roomDtoList);
        responseDto.setCode(200);
        responseDto.setInterCode(1);
        return responseDto;
    }

    @PatchMapping("/bobs/room/{roomid}")
    public ResponseDto participateRoom(HttpServletRequest req, HttpServletResponse resp,
                                       @PathVariable String roomid){
        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null)
            redirectLogin(resp);

        if (!userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        long result = roomService.enterRoom(sessionDTO.getUserId(), roomid);
        ResponseDto responseDto = new ResponseDto();
        if (result < 0L) {
            responseDto.setCode(500);
        } else {
            responseDto.setCode(200);
        }
        responseDto.setInterCode((int)result);
        return responseDto;
    }

    @DeleteMapping("/bobs/room/{roomid}")
    public ResponseDto exitRoom(HttpServletRequest req, HttpServletResponse resp,
                                @PathVariable String roomid) {
        SessionDto sessionDTO = sessionCheck(req);
        if (sessionDTO == null)
            redirectLogin(resp);

        if (!userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);
        return new ResponseDto();
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
