package com.example.projectprototype.service;

import com.example.projectprototype.entity.Ban;
import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ParticipantService {

    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;

    public List<Room> getActiveRoomList(List<Participant> participantList)
    {
        List<Room> roomList = new ArrayList<>();
        for(Participant participant: participantList)
            if (participant.getRoom().getStatus().equals(RoomStatus.active))
                roomList.add(participant.getRoom());
        return roomList;
    }

    public boolean checkValidTime(List<Participant> participantList, LocalDateTime meetTime)
    {
        Duration duration;
        for (Participant participant : participantList)
        {
            if (participant.getRoom().getStatus().equals(RoomStatus.active) &&
                    ChronoUnit.MINUTES.between(meetTime, participant.getRoom().getMeetTime()) < 60)
                return false;
        }
        return true;
    }

    public Participant enterRoom(User user, Room room)
    {
        Participant participant = new Participant();
        participant.setUser(user);
        participant.setRoom(room);
        user.getParticipantList().add(participant);
        room.getParticipantList().add(participant);
        participantRepository.save(participant);
        return participant;
    }

    public void exitRoom(User user,Room room)
    {
        Participant participant = participantRepository.findByUserAndRoom(user, room);
        user.getParticipantList().remove(participant);
        room.getParticipantList().remove(participant);
        participantRepository.delete(participant);
    }

    public boolean checkBaned(User user,Room room)
    {
        for (Participant participant : room.getParticipantList())
            for (Ban dest : participant.getUser().getBanSrcList())
                if (dest.getDest() == user)
                    return false;
        return true;
    }

    public int checkAndEnterRoom(User user, Room room)
    {
        if (!checkBaned(user,room))
            return 4; //벤 당함 (실패)
        enterRoom(user,room);
        return 1;
    }
}
