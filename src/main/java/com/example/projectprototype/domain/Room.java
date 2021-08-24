package com.example.projectprototype.domain;

import com.example.projectprototype.domain.enums.Location;
import com.example.projectprototype.domain.enums.RoomStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
@Setter
public class Room {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "meettime")
    private Date meetTime;
    private Location location;
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    private RoomStatus status;

    @OneToMany(mappedBy = "room")
    private List<RoomUser> roomUserList = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<RoomMenu> roomMenuList = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<ChatMessage> messageList = new ArrayList<>();

    public void setOwner(User user) {
        if (this.owner != null) {
            this.owner.getRoomOwnerList().remove(this);
        }
        this.owner = user;
        user.getRoomOwnerList().add(this);
    }

    public void addRoomUser(RoomUser roomUser) {
        this.roomUserList.add(roomUser);
        if (roomUser.getRoom() != this) {
            roomUser.setRoom(this);
        }
    }

    public void addRoomMenu(RoomMenu roomMenu) {
        this.roomMenuList.add(roomMenu);
        if (roomMenu.getRoom() != this) {
            roomMenu.setRoom(this);
        }
    }

    public void addMessage(ChatMessage message) {
        this.messageList.add(message);
        if (message.getRoom() != this) {
            message.setRoom(this);
        }
    }
}
