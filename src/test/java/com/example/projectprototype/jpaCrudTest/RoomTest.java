package com.example.projectprototype.jpaCrudTest;

import com.example.projectprototype.dto.RoomCreateRequestDto;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.repository.*;
import com.example.projectprototype.service.RoomService;
import com.example.projectprototype.service.RoomServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.projectprototype.jpaCrudTest.TotalTest.createTestUser;

@SpringBootTest
@Transactional
public class RoomTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BanRepository banRepository;
	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private ParticipantRepository participantRepository;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private RoomMenuRepository roomMenuRepository;
	@Autowired
	private ChatMessageRepository messageRepository;

	@Autowired
	private RoomService roomService;

	@Test
	@Rollback(false)
	public void roomCreate()
	{
		Optional<User> user = userRepository.findById("user1");
		RoomCreateRequestDto dto = new RoomCreateRequestDto();
		dto.setTitle("roomCreate4");
		dto.setStrMeetTime("2021-06-20 09:42:09");
		dto.setCapacity(4);
		dto.setLocation(Location.서초);
		List<MenuName> menuNameList = new ArrayList<>();
		//menuNameList.add(MenuName.도시락);
		menuNameList.add(MenuName.배달음식);
		//menuNameList.add(MenuName.중식);
		//menuNameList.add(MenuName.술);
		dto.setMenuNameList(menuNameList);
		roomService.roomCreate(user.get(), dto);
		// https://huisam.tistory.com/entry/mapStruct
	}
	
	//진행해야 할 목록 (우선순위 순)
	/*
	- 트랜잭셔널 함수에 붙여야하나?
	- 무한 스크롤
	- 밴 고려 안되어 있음 추가 필요
	- 컨트롤러 작성 필요
	- sessionDTO 작성 필요
	- roomDelete/roomUpdate 추가 필요(금방함) -> 지울때 즉 방에서 나갈때 방장은...?
	- roomEnter unit 테스트로 만들기
	 */
	//
}
