package com.bobsPlus.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SearchRoomResponseDto {
    private long Id;
    private String title;
    private String meetTime;
    private String location;
    private String owner;
    private int capacity;
    private String status;
    private String announcement;

    private List<String> menus = new ArrayList<>();
    private List<String> participants = new ArrayList<>();
}
