package com.example.projectprototype.service;

import com.example.projectprototype.dto.SearchRoomsRequestDto;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.util.EnumUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomServiceUtils {
    public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void defaultValueProcess(SearchRoomsRequestDto reqDto) {

        if (reqDto.getKeyword().equals("default")) reqDto.setKeyword("%");
        else {
            String tmp = reqDto.getKeyword();
            reqDto.setKeyword("%" + tmp + "%");
        }
        if (reqDto.getLocation().equals("default")) reqDto.setLocation("%");
        if (reqDto.getStartTime().equals("default") || reqDto.getEndTime().equals("default")) {
            reqDto.setStartTime(LocalDateTime.now().minusMinutes(30).format(formatter));
            reqDto.setEndTime(LocalDateTime.now().plusHours(25).format(formatter));
        }
    }

    public void getMenuName(String menu, List<String> menuNameList) {
        if (menu.equals("default")) {
            Collections.addAll(menuNameList, MenuName.getNames(MenuName.class));
        } else {
            String[] menuArr = menu.split(",");
            Collections.addAll(menuNameList, menuArr);
        }
    }

    // 입력된 시간 값이 기존 약속 시간에서 1시간 내외 안에 있는지 확인
    public boolean isValidTime(LocalDateTime time1, LocalDateTime time2) {
        if (time1.isAfter(time2)) {
            return time2.plusHours(1).isBefore(time1);
        } else {
            return time1.plusHours(1).isBefore(time2);
        }
    }

    public boolean isTimeFormat(String time) {
        if (time.equals("default")) return true;
        try {
            LocalDateTime.parse(time, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Long isRoomIdFormat(String roomId) {
        try {
            return Long.parseLong(roomId);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    // 주어진 String 이 Location enum 에 포함된 값인지 확인
    public boolean isInLocation(String location) {
        if (location.equals("default")) return true;
        try {
            EnumUtils.findEnumInsensitiveCase(Location.class, location);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 주어진 String 이 MenuName enum 에 포함된 값인지 확인
    public boolean isInMenuName(String menuName) {
        if (menuName.equals("default")) return true;
        try {
            String[] menuNameArr = menuName.split(",");
            for (String menuNameSplit : menuNameArr) {
                EnumUtils.findEnumInsensitiveCase(MenuName.class, menuNameSplit);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
