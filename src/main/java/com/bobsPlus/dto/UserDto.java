package com.bobsPlus.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String id;
    private String email;
    private String profile;
    private List<String> banSrcList = new ArrayList<>(); // 내가 벤한 사람들 id
    private List<String> banDestList = new ArrayList<>(); // 나를 벤한 사람들 id
    private List<Long> ownedRoomList = new ArrayList<>(); // 내가 방장으로 있는 방

    @Builder
    public UserDto(String id, String email, String profile) {
        this.id = id;
        this.email = email;
        this.profile = profile;
    }
}
