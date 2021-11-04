package com.bobsPlus.repository;

import com.bobsPlus.entity.Menu;
import com.bobsPlus.entity.enums.MenuName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Menu findByName(MenuName menuName);
}
