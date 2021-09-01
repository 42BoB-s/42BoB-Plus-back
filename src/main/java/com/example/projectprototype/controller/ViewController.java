package com.example.projectprototype.controller;

import com.example.projectprototype.dto.ResponseDto;
import com.example.projectprototype.dto.ViewListRequestDto;
import com.example.projectprototype.dto.ViewListResponseDto;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.repository.UserRepository;
import com.example.projectprototype.service.ViewService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="/bobs")
@AllArgsConstructor
public class ViewController {

	private final ViewService viewService;

	//임시
	private final UserRepository userRepository;

	// User는 Session으로 수정 필요

	@GetMapping("/myroom")
	public ResponseDto<List<ViewListResponseDto>> myRoomList(User user)
	{
		User u = userRepository.findById("user1").get();
		return viewService.myRoomView(u);
	}

	@GetMapping("/rooms")
	public ResponseDto<List<ViewListResponseDto>>  defaultList(User user, ViewListRequestDto dto,
	                                                           @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		// Usage : http://localhost:8080/bobs/rooms?page=0&size=5&sort=id,desc + 방 데이터(필터)
		Optional<User> test = userRepository.findById("user1");
		dto.setLocation(Location.서초); // Session에서 가져오기
		return viewService.scrollView(test.get(), dto, pageable);
	}
}
