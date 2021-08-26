package com.example.projectprototype.service;

import com.example.projectprototype.dto.RoomDTO;
import com.example.projectprototype.dto.SessionDTO;
import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.RoomMenu;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.repository.ParticipantRepository;
import com.example.projectprototype.repository.RoomRepository;
import com.example.projectprototype.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    public SessionDTO sessionCheck(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        SessionDTO sessionDTO = (SessionDTO)session.getAttribute("session");
        if (sessionDTO == null) {
            return null;
        }
        return sessionDTO;
    }

    // userId 로 select 해서 결과값이 있으면 true, 없으면 false
    public boolean userIdCheck(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            return false;
        else
            return true;
    }

    // 주어진 userId 가 참여중인 active 상태의 방을 DTO로 변환하여 반환
    public List<RoomDTO> getMyRoomList(String userId) {
       // 참여한 방 정보 확인
        List<Participant> participants =
               participantRepository.findByUser(userRepository.findById(userId).get());
       if (participants.size() ==0)
           return null;

       // 참여한 방 정보 기반으로 Room 엔티티 추출
        List<RoomDTO> roomDTOList = new ArrayList<>();
       for (Participant part : participants) {
           Room room = roomRepository.findByIdAndStatus(part.getRoom(), RoomStatus.active);
           roomDTOList.add(convertRoomDTO(room));
       }
       return roomDTOList;
    }

    public RoomDTO convertRoomDTO(Room room) {

        List<MenuName> menus = new ArrayList<>();
        for(RoomMenu roomMenu : room.getRoomMenuList()) {
            menus.add(roomMenu.getMenu().getName());
        }

        List<User> participants = new ArrayList<>();
        for (Participant part : room.getParticipantList()) {
            participants.add(part.getUser());
        }

        RoomDTO roomDTO = RoomDTO.builder()
                .roomId(room.getId())
                .title(room.getTitle())
                .menus(menus)
                .location(room.getLocation())
                .capacity(room.getCapacity())
                .owner(room.getOwner())
                .participants(participants)
                .status(room.getStatus())
                .build();
        return roomDTO;
    }
}
