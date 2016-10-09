package backend.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.MenuItemEntity;

public interface MenuItemDAO extends JpaRepository<MenuItemEntity, Integer>
{
    public MenuItemEntity findByName(String name);

    public MenuItemEntity findByCode(Integer code);

    public List<MenuItemEntity> findByActive(Boolean active);

    public List<MenuItemEntity> findByActiveAndIdNotIn(Boolean active, List<Integer> menuItemIds);
}
