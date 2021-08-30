package com.example.projectprototype.dto;

import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class ViewListResponseDto {
	private long roomId;
	private List<String> menus;
	private String title;
	private String meetTime;
	private Location location;
	private String owner; // type 변경 : User -> String
	private int capacity;
	private List<String> participants;  // type 변경 : List<User> -> List<String>
	private String status;
}
