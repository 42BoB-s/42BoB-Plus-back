package com.example.projectprototype.service;

import com.example.projectprototype.dto.RoomDto;
import com.example.projectprototype.dto.SearchRoomsRequestDto;
import com.example.projectprototype.entity.Room;
import org.springframework.data.domain.Pageable;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface RoomService {
    Long createRoom(RoomDto roomDTO, String userId);
    List<RoomDto> searchMyRooms(String userId);
    List<RoomDto> searchRooms(String userId, SearchRoomsRequestDto reqDto, Pageable pageable);
    Long enterRoom(String userId, String roomId);
    Long exitRoom(String userId, String roomId);
    Long updateTitle(String title, String roomId, String userId);
    Long paramsCheck(SearchRoomsRequestDto reqDto);
}
