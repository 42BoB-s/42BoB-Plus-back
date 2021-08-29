package com.example.projectprototype.dto;

import com.example.projectprototype.entity.RoomMenu;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.entity.enums.RoomStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomCreateRequestDto {

	//entity 및 dto를 구분해서 구현
	//https://yoonho-devlog.tistory.com/69

	private String title;
	private String strMeetTime; // 파싱필요
	private Location location;
	private int capacity;
	private List<MenuName> menuNameList;

	//ADD
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}
