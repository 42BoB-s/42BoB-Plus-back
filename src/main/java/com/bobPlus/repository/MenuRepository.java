package com.bobPlus.repository;

import com.bobPlus.entity.Menu;
import com.bobPlus.entity.enums.MenuName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Menu findByName(MenuName menuName);
}
