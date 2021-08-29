package com.example.projectprototype.service;

import com.example.projectprototype.dto.ParticipantRequestDto;
import com.example.projectprototype.dto.RoomCreateRequestDto;
import com.example.projectprototype.dto.ViewListRequestDto;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.mapper.ParticipantRequestMapper;
import com.example.projectprototype.mapper.RoomCreateRequestMapper;
import com.example.projectprototype.mapper.RoomMenuRequestMapper;
import com.example.projectprototype.repository.*;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScrollService {
	private UserRepository userRepository;
	private BanRepository banRepository;
	private RoomRepository roomRepository;
	private ParticipantRepository participantRepository;
	private MenuRepository menuRepository;
	private RoomMenuRepository roomMenuRepository;
	private RoomService roomService;

	//Mapper
	private final RoomCreateRequestMapper roomCreateRequestMapper = Mappers.getMapper(RoomCreateRequestMapper.class);
	private final ParticipantRequestMapper participantRequestMapper = Mappers.getMapper(ParticipantRequestMapper.class);
	private final RoomMenuRequestMapper roomMenuRequestMapper = Mappers.getMapper(RoomMenuRequestMapper.class);

	public List<Room> defaultView(User user, ViewListRequestDto dto, Pageable pageable)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime meetTime = LocalDateTime.parse(dto.getStrStartTime(), formatter);
		dto.setStartTime(LocalDateTime.of(
				meetTime.getYear(),
				meetTime.getMonthValue(),
				meetTime.getDayOfMonth(),
				meetTime.getHour(), 0, 0));
		dto.setEndTime(dto.getStartTime().plusHours(24)); // +24

		//추가 필요 : 자신이 이미 참여한 리스트는 빼줘야함
		List<Room> list = roomRepository.findRoomByMeetTimeBetweenAndStatusEqualsAndLocationEquals(
				dto.getStartTime(), dto.getEndTime(), RoomStatus.active, Location.서초, pageable);


		return list;
	}
}
