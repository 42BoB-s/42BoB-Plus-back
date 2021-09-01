package com.example.projectprototype.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "participant")
public class Participant extends TimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        if (this.user != null) {
            this.user.getParticipantList().remove(this);
        }
        this.user = user;
        user.getParticipantList().add(this);
    }

    public void setRoom(Room room) {
        if (this.room != null) {
            this.room.getParticipantList().remove(this);
        }
        this.room = room;
        room.getParticipantList().add(this);
    }
}
