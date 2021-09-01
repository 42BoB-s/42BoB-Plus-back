package com.example.projectprototype.controller;

import com.example.projectprototype.dto.ResponseDto;
import com.example.projectprototype.dto.RoomCreateRequestDto;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.repository.UserRepository;
import com.example.projectprototype.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequestMapping(value="/bobs")
@AllArgsConstructor
public class RoomController {

	private final RoomService roomService;

	// 임시
	private final UserRepository userRepository;

	// User는 Session으로 수정 필요

	@PostMapping("/room")
	public ResponseDto<Room> roomCreate(User user, RoomCreateRequestDto dto) {
		return roomService.roomFindMatch(user, dto);
	}

	@PutMapping(value = "/room/{roomid}")
	public ResponseDto<Room> roomEnter(User user, @PathVariable long roomid) {
		return roomService.roomEnter(userRepository.findById("user1").get(), roomid);
	}

	@DeleteMapping(value="/myroom/{roomid}")
	public ResponseDto<Room> roomExit(User user, @PathVariable long roomid) {
		return roomService.roomExit(userRepository.findById("user1").get(), roomid);
	}

	@PatchMapping(value="/room/title/{roomid}")
	public ResponseDto<Room> roomEditTitle(User user, @PathVariable long roomid, @RequestBody HashMap<String, String> map) {
		//Request Content-Type : application/json
		return roomService.roomEditTitle(userRepository.findById("user1").get(), roomid, map.get("title"));
	}
}
