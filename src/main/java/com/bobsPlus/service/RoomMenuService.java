package com.bobsPlus.service;

import com.bobsPlus.repository.MenuRepository;
import com.bobsPlus.repository.RoomMenuRepository;
import com.bobsPlus.entity.Menu;
import com.bobsPlus.entity.Room;
import com.bobsPlus.entity.RoomMenu;
import com.bobsPlus.entity.enums.MenuName;
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
