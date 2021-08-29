package com.example.projectprototype.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "room_menu")
@Getter @Setter
@NoArgsConstructor
public class RoomMenu extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
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
