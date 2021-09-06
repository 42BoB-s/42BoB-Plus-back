package com.example.projectprototype.service;

import com.example.projectprototype.entity.Menu;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.RoomMenu;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.repository.MenuRepository;
import com.example.projectprototype.repository.RoomMenuRepository;
import com.example.projectprototype.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomMenuService {

    private final RoomMenuRepository rmRepository;
    private final MenuRepository menuRepository;

    public void mappingRoomAndMenu(Room room, String menuName) {
        RoomMenu roomMenu = new RoomMenu();
        roomMenu.setRoom(room);
        roomMenu.setMenu(menuRepository.findByName(MenuName.valueOf(menuName)));
        rmRepository.save(roomMenu);
    }

    public void mappingRoomAndMenu(Room room, Menu menu) {
        RoomMenu roomMenu = new RoomMenu();
        roomMenu.setRoom(room);
        roomMenu.setMenu(menu);
        rmRepository.save(roomMenu);
    }
}
