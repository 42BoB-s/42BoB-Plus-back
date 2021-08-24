package com.example.projectprototype.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "roommenu")
@Getter
@Setter
public class RoomMenu {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roomid")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "menuid")
    private Menu menu;

    public void setRoom(Room room) {
        if (this.room != null) {
            this.room.getRoomMenuList().remove(this);
        }
        this.room = room;
        this.room.getRoomMenuList().add(this);
    }

    public void setMenu(Menu menu) {
        if (this.menu != null) {
            this.menu.getRoomMenuList().remove(this);
        }
        this.menu = menu;
        this.room.getRoomMenuList().add(this);
    }
}
