package com.example.projectprototype.domain;

import com.example.projectprototype.domain.enums.MenuName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
@Getter @Setter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menuname")
    @Enumerated(EnumType.STRING)
    private MenuName menuName;

    @OneToMany(mappedBy = "menu")
    private List<RoomMenu> roomMenuList = new ArrayList<>();

    public void addRoomMenu(RoomMenu roomMenu) {
        this.roomMenuList.add(roomMenu);
        if (roomMenu.getMenu() != this) {
            roomMenu.setMenu(this);
        }
    }
}
