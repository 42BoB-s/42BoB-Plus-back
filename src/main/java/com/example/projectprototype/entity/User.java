package com.example.projectprototype.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter @Setter
@NoArgsConstructor
public class User extends TimeEntity {
    @Id
    @Column(length = 45)
    private String id;

    @Column(length = 45)
    private String role;
    
    @Lob
    private String profile;

    @OneToMany(mappedBy = "src")
    private List<Ban> banSrcList = new ArrayList<>();

    @OneToMany(mappedBy = "dest")
    private List<Ban> banDestList = new ArrayList<>();

    // room.owner 와의 연관관계 설정
    @OneToMany(mappedBy = "owner")
    private List<Room> ownerList = new ArrayList<>();

    // room 에 참여한 정보
    @OneToMany(mappedBy = "user")
    private List<Participant> participantList = new ArrayList<>();

    public void addBanSrc(Ban ban) {
        this.banSrcList.add(ban);
        if (ban.getSrc() != this) {
            ban.setSrc(this);
        }
    }

    public void addBanDest(Ban ban) {
        this.banDestList.add(ban);
        if (ban.getDest() != this) {
            ban.setDest(this);
        }
    }

    public void addOwner(Room room) {
        this.ownerList.add(room);
        if (room.getOwner() != this) {
            room.setOwner(this);
        }
    }

    public void addParticipant(Participant participant) {
        this.participantList.add(participant);
        if (participant.getUser() != this) {
            participant.setUser(this);
        }
    }
}
