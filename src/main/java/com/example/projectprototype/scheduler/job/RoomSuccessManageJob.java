package com.example.projectprototype.scheduler.job;

import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.enums.RoomStatus;
import com.example.projectprototype.repository.RoomRepository;
import com.example.projectprototype.service.ChatService;
import com.example.projectprototype.service.ChatServiceImpl;
import org.quartz.*;

import java.time.LocalDateTime;
import java.util.List;

// active 상태이면서, 특정 시간에 걸리는 Room 들의 상태를 succeed 로 바꾸고, 관련된 채팅방을 힙 영역에서 제거하는 작업
public class RoomSuccessManageJob implements Job{

    private final RoomRepository roomRepository;
    private final ChatService chatService;

    RoomSuccessManageJob(RoomRepository roomRepository, ChatServiceImpl chatService) {
        this.roomRepository = roomRepository;
        this.chatService = chatService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime fromTime = LocalDateTime.now();
        LocalDateTime toTime = fromTime.plusMinutes(10);
        List<Room> rooms = roomRepository.findByStatusAndMeetTimeBetween(RoomStatus.active, fromTime, toTime);

        if (rooms.size() > 0) {
            for (Room room : rooms) {
                roomRepository.updateStatus(room.getId(), "succeed"); // active 상태 방을 탐색하면서 조건에 맞으면 success 상태로 바꾸기
                chatService.removeChat(room.getId()); // 해당 roomId key value 에 해당하는 메모리 영역 remove
            }
        }
    }
}
