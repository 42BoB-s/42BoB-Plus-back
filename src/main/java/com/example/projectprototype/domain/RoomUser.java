package com.example.projectprototype.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "roomuser")
@Getter @Setter
public class RoomUser {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roomid")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    public void setUser(User user) {
        if (this.user != null) {
            this.user.getRoomUserList().remove(this);
        }
        this.user = user;
        user.getRoomUserList().add(this);
    }

    public void setRoom(Room room) {
        if (this.room != null) {
            this.room.getRoomUserList().remove(this);
        }
        this.room = room;
        room.getRoomUserList().add(this);
    }
}
