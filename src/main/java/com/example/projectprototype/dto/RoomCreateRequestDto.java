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

	private String title;
	private String strMeetTime; // 파싱필요
	private Location location;
	private int capacity;
	private List<MenuName> menuNameList;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}
