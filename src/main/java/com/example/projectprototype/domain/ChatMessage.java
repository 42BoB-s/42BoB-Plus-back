package com.example.projectprototype.domain;

import com.example.projectprototype.domain.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "chatmessage")
@Getter @Setter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roomid")
    private Room room;

    private String writer;
    private String message;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "messagetype")
    private MessageType messageType;

    public void setRoom(Room room) {
        if (this.room != null) {
            this.room.getMessageList().remove(this);
        }
        this.room = room;
        room.getMessageList().add(this);
    }
}
