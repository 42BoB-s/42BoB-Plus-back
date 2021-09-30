package com.bobPlus.service;

import com.bobPlus.repository.MenuRepository;
import com.bobPlus.repository.RoomMenuRepository;
import com.bobPlus.entity.Menu;
import com.bobPlus.entity.Room;
import com.bobPlus.entity.RoomMenu;
import com.bobPlus.entity.enums.MenuName;
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
