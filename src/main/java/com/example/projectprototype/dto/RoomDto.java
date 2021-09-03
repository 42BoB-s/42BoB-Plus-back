package com.example.projectprototype.dto;

import com.example.projectprototype.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomDto {

    private long Id;
    private String title;
    private String meetTime;
    private String location;
    private UserDto owner;
    private int capacity;
    private String status;
    private String announcement;

    private List<String> menus = new ArrayList<>();
    private List<UserDto> participants = new ArrayList<>();
}
