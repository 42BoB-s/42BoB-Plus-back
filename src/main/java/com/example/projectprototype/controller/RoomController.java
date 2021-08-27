package com.example.projectprototype.controller;

import com.example.projectprototype.dto.*;
import com.example.projectprototype.service.RoomService;
import com.example.projectprototype.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class RoomController {

    @Value("${baseUrl}")
    private String baseUrl;

    private final UserService userService;
    private final RoomService roomService;

    // 예약한 방 조회
    @GetMapping("/myroom")
    public ListDTO<RoomDTO> getMyRoom(HttpServletRequest req, HttpServletResponse resp) {

        // session 값이 있는지 확인, 없으면 login 화면으로 보내기.
        SessionDTO sessionDTO = sessionCheck(req);
        if (sessionDTO == null)
            redirectLogin(resp);

        // session 값이 있으면, 주어진 session 값에 id가 등록된 회원인지 확인
        if (!userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        // 주어진 회원 정보가 존재하는 회원이면 해당 회원이 속해있는 방 정보 전달
        return roomService.getMyRoomList(sessionDTO.getUserId());
    }

    @PostMapping("/room")
    public ResponseDTO createRoom(RoomDTO roomDTO,
                                  HttpServletRequest req, HttpServletResponse resp) {
        SessionDTO sessionDTO = sessionCheck(req);
        if (sessionDTO == null)
            redirectLogin(resp);

        if (!userService.userIdCheck(sessionDTO.getUserId()))
            redirectLogin(resp);

        long result = roomService.createRoom(roomDTO, sessionDTO.getUserId());

        ResponseDTO responseDTO =
                ResponseDTO.builder()
                .code(200)
                .interCode((int)result)
                .build();
        return responseDTO;
    }

    private void redirectLogin(HttpServletResponse resp) {
        try {
            resp.sendRedirect(baseUrl + "login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public SessionDTO sessionCheck(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        SessionDTO sessionDTO = (SessionDTO)session.getAttribute("session");
        if (sessionDTO == null) {
            return null;
        }
        return sessionDTO;
    }
}
