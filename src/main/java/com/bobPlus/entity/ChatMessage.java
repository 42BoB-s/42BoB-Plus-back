package com.bobPlus.entity;

import com.bobPlus.entity.enums.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
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

    private LocalDateTime time;
}