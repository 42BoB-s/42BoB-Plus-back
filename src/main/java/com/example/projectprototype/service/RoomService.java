package com.example.projectprototype.service;

import com.example.projectprototype.dto.ParticipantRequestDto;
import com.example.projectprototype.dto.ResponseDto;
import com.example.projectprototype.dto.RoomCreateRequestDto;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;

public interface RoomService {
	Room roomInit(User user, RoomCreateRequestDto dto);
	boolean roomCreateDupleChk(User user, RoomCreateRequestDto dto);
	boolean roomEnterDupleChk(ParticipantRequestDto dto);
	ResponseDto<Room> roomFindMatch(User user, RoomCreateRequestDto dto);
	ResponseDto<Room> roomCreate(User user, RoomCreateRequestDto dto);
	ResponseDto<Room> roomEnter(User user, long roomid);
	ResponseDto<Room> roomExit(User user, long roomid);
	ResponseDto<Room> roomEditTitle(User user, long roomid, String title);

}
