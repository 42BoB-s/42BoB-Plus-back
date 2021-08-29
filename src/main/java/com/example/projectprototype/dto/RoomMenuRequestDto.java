package com.example.projectprototype.dto;

import com.example.projectprototype.entity.Menu;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.MenuName;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomMenuRequestDto {
	private Room room;
	private Menu menu;

	@Builder
	public RoomMenuRequestDto(Room room, Menu menu)
	{
		this.room = room;
		this.menu = menu;
	}
}
