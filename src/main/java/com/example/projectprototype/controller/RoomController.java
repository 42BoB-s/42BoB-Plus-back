package com.example.projectprototype.controller;

import com.example.projectprototype.dto.RoomDTO;
import com.example.projectprototype.dto.SessionDTO;
import com.example.projectprototype.repository.RoomRepository;
import com.example.projectprototype.repository.UserRepository;
import com.example.projectprototype.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoomController {

    @Value("${baseUrl}")
    private String baseUrl;

    private final RoomService roomService;

    // 예약한 방 조회
    @GetMapping("/myroom")
    @ResponseBody
    public List<RoomDTO> getMyRoom(HttpServletRequest req, HttpServletResponse resp) {

        // session 값이 있는지 확인, 없으면 login 화면으로 보내기.
        SessionDTO sessionDTO = roomService.sessionCheck(req);
        if (sessionDTO == null)
            redirectLogin(resp);

        // session 값이 있으면, 주어진 session 값에 id가 등록된 회원인지 확인
        if (roomService.userIdCheck(sessionDTO.getUserId()) == false)
            redirectLogin(resp);

        // 주어진 회원 정보가 존재하는 회원이면 해당 회원이 속해있는 방 정보 전달
        return roomService.getMyRoomList(sessionDTO.getUserId());
    }

    private void redirectLogin(HttpServletResponse resp) {
        try {
            resp.sendRedirect(baseUrl + "login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
