package com.example.projectprototype.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter @Setter
public class User {
    @Id
    @Column(name = "userid")
    private String userId;
    private String role;
    private String profile;

    // banlist.banuserid 와의 연관관계 설정
    @OneToMany(mappedBy = "src")
    private List<Ban> banList = new ArrayList<>();

    // room.owner 와의 연관관계 설정
    @OneToMany(mappedBy = "owner")
    private List<Room> roomOwnerList = new ArrayList<>();

    // room 에 참여한 정보
    @OneToMany(mappedBy = "user")
    private List<RoomUser> roomUserList = new ArrayList<>();

    public void addBan(Ban ban) {
        this.banList.add(ban);
        if (ban.getSrc() != this) {
            ban.setSrc(this);
        }
    }

    public void addRoomUser(RoomUser roomUser) {
        this.roomUserList.add(roomUser);
        if (roomUser.getUser() != this) {
            roomUser.setUser(this);
        }
    }
}
