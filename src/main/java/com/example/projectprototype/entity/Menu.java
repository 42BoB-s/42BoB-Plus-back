package com.example.projectprototype.entity;

import com.example.projectprototype.entity.enums.MenuName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "menu")
public class Menu extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45)
    @Enumerated(EnumType.STRING)
    private MenuName name;

    @OneToMany(mappedBy = "menu")
    private List<RoomMenu> roomMenuList = new ArrayList<>();

    public void addRoomMenu(RoomMenu roomMenu) {
        this.roomMenuList.add(roomMenu);
        if (roomMenu.getMenu() != this) {
            roomMenu.setMenu(this);
        }
    }
}
