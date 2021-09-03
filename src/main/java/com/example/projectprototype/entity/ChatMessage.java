package com.example.projectprototype.entity;

import com.example.projectprototype.entity.enums.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "chatmessage")
public class ChatMessage extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
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