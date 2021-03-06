package com.bobsPlus.debug;

import com.bobsPlus.dto.RoomDto;
import com.bobsPlus.dto.UserDto;
import com.bobsPlus.entity.User;
import com.bobsPlus.mapper.UserMapper;
import com.bobsPlus.repository.UserRepository;
import com.bobsPlus.service.RoomService;
import com.bobsPlus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class DebugRoomService {

    private final List<String> titleList = new ArrayList<>(List.of("title1", "title2", "title3", "title4"));
    private final List<String> idList = new ArrayList<>(List.of("tjeong_debug", "mhong_debug", "dohelee_debug", "sham_debug", "yeoncho_debug"));
    private final List<String> menuList = new ArrayList<>(List.of("한식","중식","일식","분식","커피"));

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RoomService roomService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Long debug_createRoomRandom(RoomDto roomDTO) {
        Random rand = new Random();
        LocalDateTime now = LocalDateTime.now();
        String userId = idList.get(rand.nextInt(idList.size()));

        if (!userService.userIdCheck(userId)) {
            UserDto userDto = new UserDto();
            userDto.setId(userId);
            userDto.setProfile(userId + ".jpg");
            User user = userMapper.toEntity(userDto);
            user.setRole("1");
            userRepository.save(user);
        }

        roomDTO.setLocation("서초");
        roomDTO.setCapacity(4);
        roomDTO.setTitle(titleList.get(rand.nextInt(titleList.size())));
        roomDTO.setMeetTime(now.format(formatter));
        roomDTO.setStatus("active");
        roomDTO.setAnnouncement("Announcement");
        List<String> chosenMenu = new ArrayList<>();
        chosenMenu.add(menuList.get(rand.nextInt(menuList.size())));
        roomDTO.setMenus(chosenMenu);

        return roomService.createRoom(roomDTO, userId);
    }

    public Long debug_createRoomWithData(RoomDto roomDTO, String userId) {
        if (!userService.userIdCheck(userId)) {
            UserDto userDto = new UserDto();
            userDto.setId(userId);
            userDto.setProfile(userId + ".jpg");
            User user = userMapper.toEntity(userDto);
            user.setRole("1");
            userRepository.save(user);
        }

        return roomService.createRoom(roomDTO, userId);
    }
}
