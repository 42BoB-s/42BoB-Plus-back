package com.example.projectprototype.dto;

import com.example.projectprototype.entity.User;
import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.entity.enums.RoomStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class RoomDTO {
    private long roomId;
    private String title;
    private List<MenuName> menus = new ArrayList<>();
    private Location location;
    private int capacity;
    private User owner;
    private List<User> participants = new ArrayList<>();
    private RoomStatus status;
}
