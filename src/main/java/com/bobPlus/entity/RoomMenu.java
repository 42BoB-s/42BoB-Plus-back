package com.bobPlus.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "room_menu")
public class RoomMenu extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "menu_id")
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
