package backend.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.MenuItemDetailsEntity;

public interface MenuItemDetailsDAO extends JpaRepository<MenuItemDetailsEntity, Integer>
{

}
