package com.example.projectprototype.repository;

import com.example.projectprototype.domain.Menu;
import com.example.projectprototype.domain.enums.MenuName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends CrudRepository<Menu, Long> {

    Menu findByMenuName(MenuName menuName);
}
