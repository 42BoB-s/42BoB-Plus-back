package com.example.projectprototype.jpaCrudTest;

import com.example.projectprototype.dto.RoomCreateRequestDto;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.repository.*;
import com.example.projectprototype.service.RoomService;
import com.example.projectprototype.service.RoomServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class RoomTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoomService roomService;

	@Test
	@Rollback(false)
	public void roomCreate() {
		Optional<User> user = userRepository.findById("user1");
		RoomCreateRequestDto dto = new RoomCreateRequestDto();
		dto.setTitle("roomCreate");
		dto.setStrMeetTime("2021-06-21 09:50:13");
		dto.setCapacity(4);
		dto.setLocation(Location.서초);
		List<MenuName> menuNameList = new ArrayList<>();
		menuNameList.add(MenuName.도시락);
		menuNameList.add(MenuName.배달음식);
		menuNameList.add(MenuName.중식);
		menuNameList.add(MenuName.술);
		dto.setMenuNameList(menuNameList);
		roomService.roomCreate(user.get(), dto);
		// https://huisam.tistory.com/entry/mapStruct
	}

	@Test
	@Rollback(false)
	public void roomExit()
	{
		roomService.roomExit(
				userRepository.findById("user4").get()
				, 63
		);
	}

	@Test
	@Rollback(false)
	public void roomEditTitle()
	{
		roomService.roomEditTitle(
				userRepository.findById("user4").get()
				, 64
				, "아 만사가 귀찮아~"
		);
	}

	@Test
	@Rollback(false)
	public void roomEnter()
	{
		roomService.roomEnter(
				userRepository.findById("user1").get()
				, 65
		);
	}
	
	//진행해야 할 목록 (우선순위 순)
	/*
	- 밴 고려 안되어 있음 추가 필요
	- JPA : N+1 문제 생기는지 검토 필요
	 */
	//
}
