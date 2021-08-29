package com.example.projectprototype.controller;

import com.example.projectprototype.dto.ViewListRequestDto;
import com.example.projectprototype.dto.ViewListResponseDto;
import com.example.projectprototype.entity.*;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.repository.UserRepository;
import com.example.projectprototype.service.ScrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ViewListController {

	@Autowired
	private ScrollService scrollService;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/list/defaultlist")
	public List<ViewListResponseDto> defaultList(Model model,
	                          @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

		//http://localhost:8080/list/defaultlist?page=0&size=5&sort=id,desc

		Optional<User> user = userRepository.findById("user1");
		ViewListRequestDto dto = new ViewListRequestDto();
		dto.setStrStartTime("2021-06-20 04:42:09"); //StrEndtime 필요 없는데..?

		//이대로 보내면 연관관계에 있는 객체까지 다 보내버림;;
		List<Room> list = scrollService.defaultView(user.get(), dto, pageable);

		List<ViewListResponseDto> viewListResponseDtoList = new ArrayList<>();
		for (Room r : list)
		{
			ViewListResponseDto v = new ViewListResponseDto();
			v.setRoomId(r.getId());
			List<String> menu = new ArrayList<>();
			for (RoomMenu rm : r.getRoomMenuList())
			{
				menu.add(rm.getMenu().getName().name());
			}
			v.setMenus(menu);
			v.setTitle(r.getTitle());
			//meetTime 저장할때부터 조작후에 넣어야 될듯 ex) 3:32 -> 3:00
			v.setMeetTime(r.getMeetTime().toString());
			v.setOwner(r.getOwner().getId());
			v.setLocation(r.getLocation());
			v.setCapacity(r.getCapacity());
			List<String> u = new ArrayList<>();
			for (Participant p : r.getParticipantList())
			{
				u.add(p.getUser().getId());
			}
			v.setParticipants(u);
			v.setStatus(r.getStatus().name());
			viewListResponseDtoList.add(v);
			//위에처럼 말고 toDto 메소드 사용해서 하면 안되나?
			//나중에 테스트 해보자
		}

		return viewListResponseDtoList;
	}
}
