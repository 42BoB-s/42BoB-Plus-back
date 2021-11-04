package com.bobsPlus.controller;

import com.bobsPlus.dto.SessionDto;
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

	@GetMapping("/bobs/chat/{roomId}")
	public ModelAndView chat(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse resp, @PathVariable("roomId") int roomId) {
		SessionDto sessionDTO = (SessionDto) req.getSession(false).getAttribute("session");
		modelAndView.addObject("room_id", roomId);
		modelAndView.addObject("writer", sessionDTO.getUserId());
		//modelAndView.setViewName("room"); // 임시
		modelAndView.setViewName("chat");
		return modelAndView;
	}
}