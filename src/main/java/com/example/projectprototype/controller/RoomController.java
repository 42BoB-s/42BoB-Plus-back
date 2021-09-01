package com.example.projectprototype.controller;

import com.example.projectprototype.dto.RoomDto;
import com.example.projectprototype.dto.SessionDto;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.repository.RoomRepository;
import com.example.projectprototype.service.ParticipantService;
import com.example.projectprototype.service.RoomService;
import com.example.projectprototype.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RoomController {

    @Autowired
    UserService userService;

    @Autowired
    RoomService roomService;

    @Autowired
    ParticipantService participantService;

    @Autowired
    RoomRepository roomRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // session부분 수정 필요

    // 자신이 참여한 방 정보
    @GetMapping("/myroom")
    public ResponseEntity<HashMap<String, Object>> getMyRooms(@RequestBody Map<String, String> request, HttpSession httpSession)
    {
        ResponseEntity<HashMap<String, Object>> entity = null;
        HashMap<String, Object> resultMap = new HashMap<>();

        String id = httpSession.getId();// 수정 필요
        User findUser = userService.findUserById(id);
        try {
            List<Room> roomList = participantService.getActiveRoomList(findUser.getParticipantList());
            List<RoomDto> roomDtoList = roomList.stream().map(obj->roomService.toDto(obj)).collect(Collectors.toList());
            resultMap.put("code", 1);
            resultMap.put("interCode", 1);
            resultMap.put("List<RoomDto>", roomDtoList);
             new ResponseEntity<HashMap<String, Object>>(resultMap, HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", 0);
            resultMap.put("interCode", 0);
            entity = new ResponseEntity<HashMap<String, Object>>(resultMap, HttpStatus.FORBIDDEN);
        }
       return entity;
    }

    //방 생성
    @PostMapping( "/room")
    public ResponseEntity<HashMap<String, Object>> createRoom(@RequestBody Map<String, Object> request,HttpSession httpSession)
    {
        ResponseEntity<HashMap<String, Object>> entity = null;
        HashMap<String, Object> resultMap = new HashMap<>();
        String id = httpSession.getId();// 수정 필요
        User findUser = userService.findUserById(id);
        String timeStr  =request.get("meetTime").toString();
        LocalDateTime meetTime = LocalDateTime.parse(request.get("meetTime").toString(),formatter);
        if (meetTime.isBefore(LocalDateTime.now()))
        {
            resultMap.put("code",200);
            resultMap.put("interCode",0);//시간 입력 오류
            return new ResponseEntity<HashMap<String, Object>>(resultMap,HttpStatus.OK);
        }
        Location location = Location.valueOf(request.get("location").toString());
        List<String> menuStr = (List<String>)request.get("menu");
        List<MenuName> menus = new ArrayList<>();
        for (String str : menuStr)
            menus.add(MenuName.valueOf(str));
        if (participantService.checkValidTime(findUser.getParticipantList(),meetTime))
        {
            roomService.createRoom(request.get("title").toString(),meetTime,location,
                    Integer.parseInt(request.get("capacity").toString()),findUser, "default",menus);
            resultMap.put("code",200);
            resultMap.put("interCode",1);//성공
            return new ResponseEntity<HashMap<String, Object>>(resultMap,HttpStatus.OK);
        }
        resultMap.put("code",200);
        resultMap.put("interCode",2);//시간 겹침
        return new ResponseEntity<HashMap<String, Object>>(resultMap,HttpStatus.OK);
    }

    // 방 리스트 조회 (쿼리 스트링(동적 쿼리) 필요)
    @GetMapping("/room")
    public ResponseEntity<HashMap<String, Object>> getRooms()
    {
        // 미완성
        ResponseEntity<HashMap<String, Object>> entity = null;
        HashMap<String, Object> resultMap = new HashMap<>();
//        String id = "example";
//
//        List<Room> rooms = roomService.searchRoom();
        return entity;
    }

    //방 참여
    @PatchMapping("/room/{roomid}")
    public ResponseEntity<HashMap<String, Object>> enterRoom(@PathVariable Long roomid, HttpSession httpSession)
    {
        ResponseEntity<HashMap<String, Object>> entity = null;
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 200);
        String id = httpSession.getId();// 수정 필요
        User findUser = userService.findUserById(id);
        //인원수 체크
        if (!roomService.checkCapacity(roomid))
        {
            resultMap.put("interCode", 2); //인원 초과
            new ResponseEntity<HashMap<String, Object>>(resultMap, HttpStatus.OK);
        }
        //시간 체크
        Optional<Room> room = roomRepository.findById(roomid);
        if (room.isEmpty() || room.get().getMeetTime().isBefore(LocalDateTime.now()))
        {
            resultMap.put("interCode", 3); // 입력값 오류
            return new ResponseEntity<HashMap<String, Object>>(resultMap, HttpStatus.OK);
        }
        resultMap.put("interCode", participantService.checkAndEnterRoom(findUser,room.get()));
        return new ResponseEntity<HashMap<String, Object>>(resultMap, HttpStatus.OK);
    }


    // 방 나가기
    @DeleteMapping("/myroom/{roomid}")
    public ResponseEntity<HashMap<String, Object>> exitRoom(@PathVariable Long roomid,@RequestBody Map<String, String> request)
    {
        ResponseEntity<HashMap<String, Object>> entity = null;
        String id = "mhong";// 수정 필요
        HashMap<String, Object> resultMap = roomService.exitRoom(id,roomid);
        return new ResponseEntity(resultMap,HttpStatus.OK);
    }

    //방 제목 수정
    @PutMapping("/room/title/{roomid}")
    public ResponseEntity<HashMap<String, Object>> changeRoomTitle(@RequestBody Map<String, String> request, @PathVariable Long roomid, HttpSession httpSession)
    {
        String id = httpSession.getId();// 수정 필요
        HashMap<String, Object> resultMap = roomService.changeRoomTitle(request.get(id), roomid, request.get("title"));
        return new ResponseEntity(resultMap,HttpStatus.OK);
    }

}
