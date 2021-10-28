package com.bobPlus.controller;

import com.bobPlus.dto.RoomDto;
import com.bobPlus.dto.SessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/bobs")
@RequiredArgsConstructor
public class SessionCheck {

	@GetMapping("/session")
	public ResponseEntity<HashMap<String, Object>> createRoom(HttpServletRequest req) {

		ResponseEntity<HashMap<String, Object>> entity;
		HashMap<String, Object> resultMap = new HashMap<>();
		SessionDto sessionDTO = (SessionDto) req.getSession().getAttribute("session");

		if (sessionDTO != null) {
			resultMap.put("interCode", 1);
			entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
		} else {
			resultMap.put("interCode", -99);
			entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
		}
		return entity;
	}
}
