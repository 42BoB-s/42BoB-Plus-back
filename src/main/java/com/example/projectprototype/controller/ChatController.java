package com.example.projectprototype.controller;

import com.example.projectprototype.repository.ChatMessageRepository;
import com.example.projectprototype.service.ChatServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {
	private final ChatMessageRepository chatRoomRepository;
	private final ChatServiceImpl chatService;

	//@GetMapping("/rooms/{id}")
	@GetMapping("/rooms") // 테스트 용 (추후 위와 같이 수정 필요 + 세션)
	public String chatRoom(@RequestParam Long room_id, @RequestParam String writer, Model model){
		// http://localhost:8080/rooms?room_id=78&writer=user1

		//예정
		//ResponseDto -> ResponseEntity 변경
		return "room";
	}
}