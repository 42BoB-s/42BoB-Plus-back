package com.bobsPlus.controller;

import com.bobsPlus.dto.UserDto;
import com.bobsPlus.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

	private final TokenService tokenService;

	@GetMapping("/bobs/chat/{roomId}")
	public ModelAndView chat(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse resp, @PathVariable("roomId") int roomId) {
		UserDto userDto = tokenService.getToken(req);
		modelAndView.addObject("room_id", roomId);
		modelAndView.addObject("writer", userDto.getId());
		//modelAndView.setViewName("room"); // 임시
		modelAndView.setViewName("chat");
		return modelAndView;
	}
}