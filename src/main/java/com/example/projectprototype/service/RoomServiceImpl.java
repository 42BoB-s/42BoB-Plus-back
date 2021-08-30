package com.example.projectprototype.service;

import com.example.projectprototype.dto.ParticipantRequestDto;
import com.example.projectprototype.dto.ResponseDto;
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
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

	private final UserRepository userRepository;
	private final BanRepository banRepository;
	private final RoomRepository roomRepository;
	private final ParticipantRepository participantRepository;
	private final MenuRepository menuRepository;
	private final RoomMenuRepository roomMenuRepository;
	private final RoomCreateRequestMapper roomCreateRequestMapper = Mappers.getMapper(RoomCreateRequestMapper.class);
	private final ParticipantRequestMapper participantRequestMapper = Mappers.getMapper(ParticipantRequestMapper.class);
	private final RoomMenuRequestMapper roomMenuRequestMapper = Mappers.getMapper(RoomMenuRequestMapper.class);

	public Room roomInit(User user, RoomCreateRequestDto dto)
	{
		Room room = roomCreateRequestMapper.toEntity(dto);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime meetTime = LocalDateTime.parse(dto.getStrMeetTime(), formatter);
		room.setOwner(user);
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
		Room r = roomRepository.findRoomByStatusEqualsAndMeetTimeBetweenAndOwner(RoomStatus.active, dto.getStartTime(), dto.getEndTime(), user);
		if(r != null)
			return true; // 이미 생성한 방이 존재
		else
			return false;
	}

	public boolean roomEnterDupleChk(ParticipantRequestDto dto)
	{
		Participant p = participantRepository.findParticipantByRoomEqualsAndUserEquals(dto.getRoom(), dto.getUser());
		if(p != null)
			return true; // 이미 참여한 방이 존재
		else
			return false;
	}

	public ResponseDto<Room> roomFindMatch(User user, RoomCreateRequestDto dto)
	{
		ParticipantRequestDto participantRequestDto = null;
		// 시간 체크 & status == active & location 체크
		List<Room> roomList = roomRepository.findRoomByMeetTimeBetweenAndStatusEqualsAndLocationEqualsOrderByIdAsc(dto.getStartTime(), dto.getEndTime(), RoomStatus.active, dto.getLocation());
		for (Room room : roomList)
		{
			// 4(캐퍼시티)와 같거나 보다 적은인원이 들어 있어야함.
			if (dto.getCapacity() > room.getParticipantList().size())
			{
				// 방에 이미 참가했는지 확인
				if (roomEnterDupleChk(ParticipantRequestDto.builder().user(user).room(room).build())) {
					System.err.println("같은 시간대에 참여중인 방이 있음");
					return ResponseDto.<Room>builder().interCode(-1).build();
				}
				// 메뉴 체크
				for (RoomMenu rm : room.getRoomMenuList())
				{
					for (MenuName menuName : dto.getMenuNameList()) {
						if (menuName.equals(rm.getMenu().getName()))
							return ResponseDto.<Room>builder().interCode(1).component(room).build();
					}
				}
			}
		}
		System.out.println("매칭되는 방이 없음");
		return ResponseDto.<Room>builder().interCode(0).build(); // 매칭되는 방이 없음
	}

	//User 객체는 Session 객체와 연동시킬 필요 있음
	public ResponseDto<Room> roomCreate(User user, RoomCreateRequestDto dto)
	{
		ParticipantRequestDto participantRequestDto;
		ResponseDto<Room> findRoom;
		Room room = roomInit(user, dto);
		// 같은 시간대에 생성된게 있는지 확인 후 저장
		if (!roomCreateDupleChk(user, dto))
		{
			// 매칭 가능한 방 체크
			findRoom = roomFindMatch(user, dto);
			if (findRoom.getInterCode() == 0) // 매칭 방이 없을때
			{
				System.out.println("방 생성 성공");
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
			} else if (findRoom.getInterCode() == 1) // 매칭 방이 있을때
				room = findRoom.getComponent();
			else // 에러 발생
				return ResponseDto.<Room>builder().interCode(findRoom.getInterCode()).build();
		}
		else
		{
			System.err.println("같은 시간대에 생성한 방이 있음");
			return ResponseDto.<Room>builder().interCode(-2).build(); // 같은 시간대에 생성한 방이 있음
		}
		//방 참가
		participantRequestDto = ParticipantRequestDto.builder().user(user).room(room).build();
		participantRepository.save(participantRequestMapper.toEntity(participantRequestDto));
		return ResponseDto.<Room>builder().interCode(1).build();
	}

	public ResponseDto<Room> roomEnter(User user, long roomid) {
		Room r = null;
		try {
			r = roomRepository.findById(roomid).get();
		} catch (NoSuchElementException e) {
			if (r == null)
			{
				System.err.println("존재 하지 않는 방");
				return ResponseDto.<Room>builder().interCode(-7).build();
			}
		}
		ParticipantRequestDto dto = ParticipantRequestDto.builder().user(user).room(r).build();
		Participant participant = participantRequestMapper.toEntity(dto);
		if (!r.getStatus().equals(RoomStatus.active))
		{
			System.err.println("활성화 된 방이 아님");
			return ResponseDto.<Room>builder().interCode(-4).build();
		}
		else if (r.getParticipantList().size() >= 4)
		{
			System.err.println("인원이 가득 찬 방");
			return ResponseDto.<Room>builder().interCode(-5).build();
		}
		else if (roomEnterDupleChk(dto))
		{
			System.err.println("이미 참여중인 방");
			return ResponseDto.<Room>builder().interCode(-6).build();
		}
		else {
			participantRepository.save(participant);
			return ResponseDto.<Room>builder().interCode(1).build();
		}
	}

	public ResponseDto<Room> roomExit(User user, long roomid) {

		User u = null;
		Room r = null;
		Participant participant = null;
		try {
			u = userRepository.findById(user.getId()).get();
			r = roomRepository.findById(roomid).get();
			participant = participantRepository.findParticipantByRoomEqualsAndUserEquals(r, u);
		} catch (Exception e) {
			System.err.println("잘못된 접근");
			return ResponseDto.<Room>builder().interCode(-3).build();
		}
		if (!r.getStatus().equals(RoomStatus.active))
		{
			System.err.println("활성화 된 방이 아님");
			return ResponseDto.<Room>builder().interCode(-4).build();
		}
		// owner 방장이 나갔을때
		if (r.getOwner() == u) // 방장이 자신일 경우
		{
			List<Participant> p_list = participantRepository.findByRoom(r);
			if (p_list.size() == 1) // 방에 자신 밖에 없을 경우
			{
				r.setStatus(RoomStatus.failed);
				roomRepository.save(r);
				System.out.println("status 변경 완료");
			}
			else
			{
				// 다른 사람으로 owner 업데이트
				for (Participant p : p_list)
				{
					if (r.getOwner() != p.getUser())
					{
						r.setOwner(p.getUser());
						roomRepository.save(r);
						System.out.println("owner 변경 완료");
						break ;
					}
				}
			}
		}
		participantRepository.delete(participant);
		System.out.println("participant 제거 완료");
		return ResponseDto.<Room>builder().interCode(1).build();
	}

	public ResponseDto<Room> roomEditTitle(User user, long roomid, String title) {
		User u = null;
		Room r = null;
		try {
			u = userRepository.findById(user.getId()).get();
			r = roomRepository.findById(roomid).get();
		} catch (NoSuchElementException e) {
			System.err.println("잘못된 접근");
			return ResponseDto.<Room>builder().interCode(-3).build();
		}
		if (!r.getStatus().equals(RoomStatus.active))
		{
			System.err.println("활성화 된 방이 아님");
			return ResponseDto.<Room>builder().interCode(-4).build();
		}
		else if (r.getOwner() != u)
		{
			System.err.println("수정 권한이 없는 사용자");
			return ResponseDto.<Room>builder().interCode(-5).build();
		}
		else
		{
			r.setTitle(title);
			roomRepository.save(r);
		}
		return ResponseDto.<Room>builder().interCode(1).build();
	}
}
