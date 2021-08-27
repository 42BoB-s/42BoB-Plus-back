package com.example.projectprototype.dto;

import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.RoomStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RoomDTO {
    private long roomId;
    private String title;
    private List<String> menus;
    private String meetTime;
    private String location;
    private int capacity;
    private User owner;
    private List<User> participants;
    private String status;
}
