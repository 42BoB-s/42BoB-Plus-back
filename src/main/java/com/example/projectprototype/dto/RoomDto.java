package com.example.projectprototype.dto;

import com.example.projectprototype.entity.Menu;
import com.example.projectprototype.entity.RoomMenu;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.entity.enums.RoomStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long roomId;
    private String title;
    private List<String> menus;
    private LocalDateTime meetTime;
    private Location location;
    private int capacity;
    private UserDto owner;
    private List<UserDto> participants;
    private RoomStatus status;

    public void setStatus() {
        this.status = RoomStatus.active;
    }
}
