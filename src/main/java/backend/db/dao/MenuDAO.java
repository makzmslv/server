package backend.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import backend.db.entity.CategoryEntity;
import backend.db.entity.MenuEntity;
import backend.db.entity.MenuItemEntity;

public interface MenuDAO extends JpaRepository<MenuEntity, Integer>
{
    public List<MenuEntity> findByMenuItem(MenuItemEntity menuItem);

    public List<MenuEntity> findByCategory(CategoryEntity category);

    @Query(value = "SELECT menuEntry.menuItem.id FROM MenuEntity menuEntry WHERE menuEntry.id IS NOT NULL")
    public List<Integer> getMenuItemIds();

    public MenuEntity findByMenuItemAndCategory(MenuItemEntity menuItem, CategoryEntity category);
}
