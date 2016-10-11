package backend.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import backend.db.entity.CategoryEntity;
import backend.db.entity.MenuEntriesEntity;
import backend.db.entity.MenuItemEntity;

public interface MenuItemListDAO extends JpaRepository<MenuEntriesEntity, Integer>
{
    public List<MenuEntriesEntity> findByMenuItem(MenuItemEntity menuItem);

    public List<MenuEntriesEntity> findByCategory(CategoryEntity category);

    @Query(value = "SELECT menuEntry.menuItem.id FROM MenuEntriesEntity menuEntry WHERE menuEntry.id IS NOT NULL")
    public List<Integer> getMenuItemIds();

    public MenuEntriesEntity findByMenuItemAndCategory(MenuItemEntity menuItem, CategoryEntity category);
}
