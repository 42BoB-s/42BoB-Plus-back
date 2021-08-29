package com.example.projectprototype.service;

import com.example.projectprototype.dto.ParticipantRequestDto;
import com.example.projectprototype.dto.RoomCreateRequestDto;
import com.example.projectprototype.dto.RoomMenuRequestDto;
import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.RoomMenu;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.mapper.ParticipantRequestMapper;
import com.example.projectprototype.mapper.RoomCreateRequestMapper;
import com.example.projectprototype.mapper.RoomMenuRequestMapper;
import com.example.projectprototype.repository.*;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomService implements RoomServiceImpl {

	private UserRepository userRepository;
	private BanRepository banRepository;
	private RoomRepository roomRepository;
	private ParticipantRepository participantRepository;
	private MenuRepository menuRepository;
	private RoomMenuRepository roomMenuRepository;

	//Mapper
	private final RoomCreateRequestMapper roomCreateRequestMapper = Mappers.getMapper(RoomCreateRequestMapper.class);
	private final ParticipantRequestMapper participantRequestMapper = Mappers.getMapper(ParticipantRequestMapper.class);
	private final RoomMenuRequestMapper roomMenuRequestMapper = Mappers.getMapper(RoomMenuRequestMapper.class);

	public Room roomInit(User user, RoomCreateRequestDto dto)
	{
		Room room = roomCreateRequestMapper.toEntity(dto);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime meetTime = LocalDateTime.parse(dto.getStrMeetTime(), formatter);
		room.setOwner(user); // mapper에서 처리 가능하나?
		room.setStatus(RoomStatus.active);
		room.setAnnouncement(""); // null 이면 에러나와서 "" 추가
		dto.setStartTime(LocalDateTime.of(
				meetTime.getYear(),
				meetTime.getMonthValue(),
				meetTime.getDayOfMonth(),
				meetTime.getHour(), 0, 0));
		dto.setEndTime(dto.getStartTime().plusHours(1));
		room.setMeetTime(dto.getStartTime());
		return room;
	}

	public boolean roomCreateDupleChk(User user, RoomCreateRequestDto dto)
	{
		Room r = roomRepository.findRoomByMeetTimeBetweenAndOwner(dto.getStartTime(), dto.getEndTime(), user);
		if(r != null)
			return true; // 중복됨
		else
			return false;
	}

	public boolean roomEnterDupleChk(ParticipantRequestDto dto)
	{
		Participant p = participantRepository.findParticipantByRoomEqualsAndUserEquals(dto.getRoom(), dto.getUser());
		if(p != null)
			return true; // 중복됨
		else
			return false;
	}

	public List<Room> roomFindMatch(User user, RoomCreateRequestDto dto)
	{
		ParticipantRequestDto participantRequestDto = null;
		// 시간 체크 & status == active & location 체크
		List<Room> roomList = roomRepository.findRoomByMeetTimeBetweenAndStatusEqualsAndLocationEqualsOrderByIdAsc(dto.getStartTime(), dto.getEndTime(), RoomStatus.active, dto.getLocation());
		List<Room> result = new ArrayList<>();
		for (Room room : roomList)
		{
			// 4(캐퍼시티)와 같거나 보다 적은인원이 들어 있어야함. && 방에 이미 참가했는지 확인
			if (dto.getCapacity() > room.getParticipantList().size())
			{
				// 메뉴 체크
				for (RoomMenu rm : room.getRoomMenuList())
				{
					for (MenuName menuName : dto.getMenuNameList()) {
						if (menuName.equals(rm.getMenu().getName()))
							result.add(room);
					}
				}
			}
		}
		return result;
	}

	//User 객체는 Session 객체와 연동시킬 필요 있음
	public int roomCreate(User user, RoomCreateRequestDto dto)
	{
		ParticipantRequestDto participantRequestDto = null;
		Room room = roomInit(user, dto);
		// 같은 시간대에 생성된게 있는지 확인 후 저장
		List<Room> findRooms = null;
		if (!roomCreateDupleChk(user, dto))
		{
			// 매칭 가능한 방 체크
			findRooms = roomFindMatch(user, dto);
			for (Room r : findRooms) {
				if (roomEnterDupleChk(ParticipantRequestDto.builder().user(user).room(r).build())) {
					System.out.println("같은 시간대에 참여중인 방이 있음");
					return (-1);
				}
			}
			if (findRooms.size() == 0)
			{
				roomRepository.save(room);
				// RoomMenu에 추가
				for (MenuName menuName : dto.getMenuNameList())
				{
					RoomMenuRequestDto roomMenuRequestDto = RoomMenuRequestDto.builder()
							.menu(menuRepository.findByName(menuName))
							.room(room)
							.build();
					RoomMenu roomMenu = roomMenuRequestMapper.toEntity(roomMenuRequestDto);
					roomMenuRepository.save(roomMenu);
				}
			}
			else
				room = findRooms.get(0); // 매칭 가능한 리스트중에 0번째 인덱스 설정 (정렬필요)
		}
		else
		{
			System.out.println("같은 시간대에 생성한 방이 있음");
			return (-2);
		}

		//방 참가
		participantRequestDto = ParticipantRequestDto.builder().user(user).room(room)
				.build();
		return (roomEnter(participantRequestDto));
	}

	public int roomEnter(ParticipantRequestDto dto) {
		Participant participant = participantRequestMapper.toEntity(dto);

		//이미 참여중인 방인지 체크
		if (!roomEnterDupleChk(dto))
			participantRepository.save(participant);
		else {
			System.out.println("이미 참여중인 방임"); //위에서 체크하는데 여기에서 에러 처리 필요해??
			return 0; // 참가 실패
		}
		return 1;
	}
}
