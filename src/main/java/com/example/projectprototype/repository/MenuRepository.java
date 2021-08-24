package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Menu;
import com.example.projectprototype.entity.enums.MenuName;
import org.springframework.data.repository.CrudRepository;

public interface MenuRepository extends CrudRepository<Menu, Long> {

    Menu findByMenuName(MenuName menuName);
}
