package com.example.projectprototype.entity;

import com.example.projectprototype.entity.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "chatmessage")
@Getter @Setter
@NoArgsConstructor
public class ChatMessage extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    @JoinColumn(name = "room_id")
    private Room room;

    private String writer;
    private String message;
    
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    public void setRoom(Room room) {
        if (this.room != null) {
            this.room.getMessageList().remove(this);
        }
        this.room = room;
        room.getMessageList().add(this);
    }
}