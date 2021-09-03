package com.example.projectprototype.repository;

import com.example.projectprototype.entity.Menu;
import com.example.projectprototype.entity.enums.MenuName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Menu findByName(MenuName menuName);
}
