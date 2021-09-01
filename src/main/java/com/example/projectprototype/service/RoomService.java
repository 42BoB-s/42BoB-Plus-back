package com.example.projectprototype.service;

import com.example.projectprototype.dto.RoomDto;
import com.example.projectprototype.entity.*;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.mapper.RoomMapper;
import com.example.projectprototype.repository.RoomRepository;
import com.example.projectprototype.repository.UserRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserService userService;

    @Autowired
    ParticipantService participantService;

    @Autowired
    RoomMenuService roomMenuService;

    private final RoomMapper roomMapper = Mappers.getMapper(RoomMapper.class);

    public RoomDto toDto (Room room)
    {
        return roomMapper.toDto(room);
    }

    public boolean checkOwner(String userid, Long roomid)
    {
        Optional<Room> room = roomRepository.findById(roomid);
        if (room.isPresent())
            return (room.get().getOwner().getId().equals(userid));
        return false;
    }

    public HashMap<String, Object> changeRoomTitle(String userid,Long roomid, String title){
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 200);
        if (checkOwner(userid,roomid))
        {
            Optional<Room> room = roomRepository.findById(roomid);
            room.get().setTitle(title);
            roomRepository.save(room.get());
            resultMap.put("interCode", 1);//성공
            return resultMap;
        }
        resultMap.put("interCode", 0);//실패
        return resultMap;
    }

    public Room createRoom(String title, LocalDateTime meetTime, Location location,
                           int capacity, User owner, String announcement, List<MenuName> menuNameList)
    {
        //roomMenuList 설정 필요
        Room room = new Room();
        room.setTitle(title);
        room.setMeetTime(meetTime);
        room.setLocation(location);
        room.setCapacity(capacity);
        room.setStatus(RoomStatus.active);
        room.setAnnouncement(announcement);

        room.setOwner(owner);
        owner.getOwnerList().add(room);
        roomRepository.save(room);

        Participant participant = participantService.enterRoom(owner, room);
        room.addRoomUser(participant);
        List<RoomMenu> menus = roomMenuService.setRoomMenuList(room,menuNameList);
        room.setRoomMenuList(menus);
        return room;
    }

    public HashMap<String, Object> exitRoom(String userid, Long roomid)
    {
        HashMap<String, Object> resultMap = new HashMap<>();
        User findUser = userService.findUserById(userid);
        Room findRoom = roomRepository.findById(roomid).get();
        resultMap.put("code", 200);
        //participant 삭제
        participantService.exitRoom(findUser,findRoom);
        if (findRoom.getOwner() == findUser) {
            //방장 넘기기
            findUser.getOwnerList().remove(findRoom);
            User newOwner = findRoom.getParticipantList().get(0).getUser();
            findRoom.setOwner(newOwner);
            newOwner.getOwnerList().add(findRoom);
        }
        if (findRoom.getParticipantList().size() == 0)
        {
            //방 삭제
            roomRepository.delete(findRoom);
            resultMap.put("intercode", 1); //방 삭제
        }
        else resultMap.put("intercode", 2); // 회원 퇴장
        return resultMap;
    }

    public boolean checkCapacity(Long roomid)
    {
        Optional<Room> room = roomRepository.findById(roomid);
        if(room.isPresent() &&
                (room.get().getParticipantList().size() < room.get().getCapacity()))
            return true;
        return false;
    }

//    public List<Room> searchRoom()
//    {
//
//    }

}
