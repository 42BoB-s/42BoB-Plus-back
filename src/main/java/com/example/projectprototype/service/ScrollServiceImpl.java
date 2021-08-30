package com.example.projectprototype.service;

import com.example.projectprototype.dto.*;
import com.example.projectprototype.entity.Menu;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.mapper.ViewListResponseMapper;
import com.example.projectprototype.repository.*;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScrollServiceImpl implements ScrollService {
	private final RoomRepository roomRepository;
	private final MenuRepository menuRepository;
	private final ViewListResponseMapper viewListResponseMapper = Mappers.getMapper(ViewListResponseMapper.class);

	public void scrollInit(ViewListRequestDto dto) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		//if (dto.getLocation() == null)
		//	dto.setLocation("세션"); 파라미터에 없는 경우 Session에서 가져오기
		if (dto.getMenu() == null) {
			List<String> menuList = new ArrayList<>();
			for (Menu menu : menuRepository.findAll()) {
				menuList.add(menu.getName().name());
			}
			dto.setMenu(menuList);
		}
		if (dto.getStrStartTime() == null || dto.getStrEndTime() == null) {
			LocalDateTime startTime = LocalDateTime.parse("2021-06-20 10:49:13", formatter); // 테스트용
			//LocalDateTime startTime = LocalDateTime.now();
			dto.setStartTime(LocalDateTime.of(
					startTime.getYear(),
					startTime.getMonthValue(),
					startTime.getDayOfMonth(),
					startTime.getHour(), 0, 0));
			dto.setEndTime(dto.getStartTime().plusHours(24)); // +24
		}
		else
		{
			LocalDateTime startTime = LocalDateTime.parse(dto.getStrStartTime(), formatter);
			dto.setStartTime(LocalDateTime.of(
					startTime.getYear(),
					startTime.getMonthValue(),
					startTime.getDayOfMonth(),
					startTime.getHour(), 0, 0));
			LocalDateTime endTime = LocalDateTime.parse(dto.getStrEndTime(), formatter);
			dto.setEndTime(LocalDateTime.of(
					endTime.getYear(),
					endTime.getMonthValue(),
					endTime.getDayOfMonth(),
					endTime.getHour(), 0, 0));
			dto.setEndTime(dto.getEndTime());
		}
		if (dto.getKeyword() == null)
			dto.setKeyword("%%");
		else
			dto.setKeyword("%" + dto.getKeyword() + "%");
	}

	public ResponseDto<List<ViewListResponseDto>> scrollView(User user, ViewListRequestDto dto, Pageable pageable)
	{
		scrollInit(dto);
		List<ViewListResponseDto> viewListResponseDtoList = new ArrayList<>();
		List<Room> list = roomRepository.findDefaultView(
				user.getId(),
				dto.getLocation().name(),
				dto.getStartTime(),
				dto.getEndTime(),
				dto.getKeyword(),
				dto.getMenu(),
				pageable);
		for (Room r : list)
		{
			ViewListResponseDto viewListResponseDto = viewListResponseMapper.toDto(r);
			viewListResponseDtoList.add(viewListResponseDto);
		}
		return ResponseDto.<List<ViewListResponseDto>>builder()
				.interCode(1)
				.component(viewListResponseDtoList).build();
	}



}
