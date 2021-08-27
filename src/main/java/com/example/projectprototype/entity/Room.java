package com.example.projectprototype.entity;

import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.RoomStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
@Setter
public class Room extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45)
    private String title;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime meetTime;

    @Enumerated(EnumType.STRING)
    private Location location;
    
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    private RoomStatus status;

    @Lob
    private String announcement; //? announcement 가 ChatRoom 이 아니라 Room에 있는게 맞나?

    @OneToMany(mappedBy = "room")
    private List<Participant> participantList = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<RoomMenu> roomMenuList = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<ChatMessage> messageList = new ArrayList<>();

    public void setOwner(User user) {
        if (this.owner != null) {
            this.owner.getOwnerList().remove(this);
        }
        this.owner = user;
        user.getOwnerList().add(this);
    }

    public void addParticipant(Participant participant) {
        this.participantList.add(participant);
        if (participant.getRoom() != this) {
            participant.setRoom(this);
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
