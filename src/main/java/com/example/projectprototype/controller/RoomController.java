package com.example.projectprototype.controller;

import com.example.projectprototype.dto.ResponseDto;
import com.example.projectprototype.dto.RoomCreateRequestDto;
import com.example.projectprototype.dto.ViewListRequestDto;
import com.example.projectprototype.dto.ViewListResponseDto;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.repository.UserRepository;
import com.example.projectprototype.service.RoomService;
import com.example.projectprototype.service.RoomServiceImpl;
import com.example.projectprototype.service.ScrollService;
import com.example.projectprototype.service.ScrollServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="/bobs")
@AllArgsConstructor
public class RoomController {

	private final RoomService roomService;
	private final ScrollService scrollService;

	//임시
	private final UserRepository userRepository;

	//User는 Session으로 수정 필요
	@PostMapping("/room")
	public ResponseDto<Room> roomCreate(User user, RoomCreateRequestDto dto) {
		return roomService.roomFindMatch(user, dto);
	}

	@PatchMapping(value = "/bobs/room/{roomid}")
	public ResponseDto<Room> roomEnter(User user, @PathVariable long roomid)
	{return roomService.roomEnter(userRepository.findById("user1").get(), roomid);

	}

	@DeleteMapping(value="/myroom/{roomid}")
	public ResponseDto<Room> roomExit(User user, @PathVariable long roomid) {
		return roomService.roomExit(userRepository.findById("user1").get(), roomid);
	}

	@PutMapping(value="/room/title/{roomid}")
	public ResponseDto<Room> roomEditTitle(User user, @PathVariable long roomid, @RequestBody String title) {
		return roomService.roomEditTitle(userRepository.findById("user1").get(), roomid, title);
	}

	//작성중
	@GetMapping("/bobs/myroom")
	public ResponseDto<List<ViewListResponseDto>> myRoomList(User user)
	{
		//최근꺼만??? active만?? 완료된것은? 마이페이지에 보여주는거야? 메인에보여주는거야? 명확하지 않네
		return null;
	}

	@GetMapping("/rooms")
	public ResponseDto<List<ViewListResponseDto>>  defaultList(User user, ViewListRequestDto dto,
	                                             @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		// Usage : http://localhost:8080//bobs/rooms?page=0&size=5&sort=id,desc + 방 데이터

		////임시////
		Optional<User> test = userRepository.findById("user4");
		dto.setLocation(Location.서초); // Session에서 가져오기

		return scrollService.scrollView(test.get(), dto, pageable);
	}
}
