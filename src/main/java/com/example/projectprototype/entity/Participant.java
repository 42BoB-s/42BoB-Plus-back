package com.example.projectprototype.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "participant")
@Getter @Setter
@NoArgsConstructor
public class Participant extends TimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    private Room room;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
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
