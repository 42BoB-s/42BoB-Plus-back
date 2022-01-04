package com.bobsPlus.service;

import com.bobsPlus.dto.RoomDto;
import com.bobsPlus.dto.SearchRoomResponseDto;
import com.bobsPlus.dto.SearchRoomsRequestDto;
import com.bobsPlus.entity.Room;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface RoomService {
    SearchRoomResponseDto getRoomInfo(long roomId);
    Long createRoom(RoomDto roomDTO, String userId);
    List<RoomDto> searchMyRooms(String userId);
    List<RoomDto> searchRooms(String userId, SearchRoomsRequestDto reqDto, Pageable pageable);
    List<RoomDto> searchRooms(SearchRoomsRequestDto reqDto, Pageable pageable);
    Long enterRoom(String userId, String roomId);
    Long exitRoom(String userId, String roomId);
    Long updateTitle(String title, String roomId, String userId);
    Long paramsCheck(SearchRoomsRequestDto reqDto);
}
