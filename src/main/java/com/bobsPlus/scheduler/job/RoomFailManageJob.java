package com.bobsPlus.scheduler.job;

import com.bobsPlus.entity.Room;
import com.bobsPlus.entity.enums.RoomStatus;
import com.bobsPlus.repository.RoomRepository;
import com.bobsPlus.service.ChatService;
import com.bobsPlus.service.ChatServiceImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

// fail 상태의 방의 채팅방을 삭제함.
public class RoomFailManageJob implements Job {

    private final RoomRepository roomRepository;
    private final ChatService chatService;

    RoomFailManageJob(RoomRepository roomRepository, ChatServiceImpl chatService) {
        this.roomRepository = roomRepository;
        this.chatService = chatService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Room> rooms = roomRepository.findByStatus(RoomStatus.failed);
        if (rooms.size() > 0) {
            for (Room room : rooms) {
                roomRepository.updateStatus(room.getId(), "managedFail");
                chatService.removeChat(room.getId());
            }
        }
    }
}