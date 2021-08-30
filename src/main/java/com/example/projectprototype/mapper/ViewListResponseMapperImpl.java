package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.ViewListResponseDto;
import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.RoomMenu;

import java.util.ArrayList;
import java.util.List;

public class ViewListResponseMapperImpl implements ViewListResponseMapper {

	public ViewListResponseDto toDto(Room r) {
		if (r == null)
			return null;
		else
		{
			ViewListResponseDto v = new ViewListResponseDto();
			v.setRoomId(r.getId());
			List<String> menu = new ArrayList<>();
			for (RoomMenu rm : r.getRoomMenuList())
			{
				menu.add(rm.getMenu().getName().name());
			}
			v.setMenus(menu);
			v.setTitle(r.getTitle());
			v.setMeetTime(r.getMeetTime().toString());
			v.setOwner(r.getOwner().getId());
			v.setLocation(r.getLocation());
			v.setCapacity(r.getCapacity());
			List<String> u = new ArrayList<>();
			for (Participant p : r.getParticipantList())
			{
				u.add(p.getUser().getId());
			}
			v.setParticipants(u);
			v.setStatus(r.getStatus().name());
			return v;
		}
	}
}
