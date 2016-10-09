package backend.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.MenuItemUnitEntity;

public interface MenuItemUnitDAO extends JpaRepository<MenuItemUnitEntity, Integer>
{

}
