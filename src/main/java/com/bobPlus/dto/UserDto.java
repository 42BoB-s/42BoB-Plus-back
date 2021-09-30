package com.bobPlus.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String id;
    private String profile;
    private List<String> banSrcList = new ArrayList<>(); // 내가 벤한 사람들 id
    private List<String> banDestList = new ArrayList<>(); // 나를 벤한 사람들 id
    private List<Long> ownedRoomList = new ArrayList<>(); // 내가 방장으로 있는 방
}
