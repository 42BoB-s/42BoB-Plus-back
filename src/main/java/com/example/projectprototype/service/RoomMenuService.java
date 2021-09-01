package com.example.projectprototype.service;

import com.example.projectprototype.entity.Menu;
import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.RoomMenu;
import com.example.projectprototype.entity.enums.MenuName;
import com.example.projectprototype.repository.MenuRepository;
import com.example.projectprototype.repository.RoomMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomMenuService {
    @Autowired
    RoomMenuRepository roomMenuRepository;

    @Autowired
    MenuRepository menuRepository;

//    public String getMenuName(RoomMenu roomMenu)
//    {
//        return roomMenu.getMenu().getName();
//    }
    public List<RoomMenu> setRoomMenuList(Room room, List<MenuName> menus)
    {
        List<RoomMenu> roomMenuList = new ArrayList<>();
        for (MenuName menuName: menus)
        {
            RoomMenu roomMenu = new RoomMenu();
            Menu menu = menuRepository.findByName(menuName);
            roomMenu.setRoom(room);
            roomMenu.setMenu(menu);
            roomMenuRepository.save(roomMenu);
            roomMenuList.add(roomMenu);
        }
        return roomMenuList;
    }

}
