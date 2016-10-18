package backend.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import backend.db.entity.CategoryEntity;
import backend.db.entity.HotelMenuEntity;
import backend.db.entity.MenuEntriesEntity;
import backend.db.entity.MenuItemEntity;

public interface MenuItemListDAO extends JpaRepository<MenuEntriesEntity, Integer>
{
    public List<MenuEntriesEntity> findByMenuItem(MenuItemEntity menuItem);

    public List<MenuEntriesEntity> findByCategory(CategoryEntity category);

    public List<MenuEntriesEntity> findByHotelMenuOrderByCategoryDisplayRankAsc(HotelMenuEntity hotelMenu);

    @Query(value = "SELECT menuEntry.menuItem.id FROM MenuEntriesEntity menuEntry WHERE menuEntry.id IS NOT NULL")
    public List<Integer> getMenuItemIds();

    @Query(value = "SELECT menuEntry.menuItem FROM MenuEntriesEntity menuEntry WHERE menuEntry.category = :category")
    public List<MenuItemEntity> getMenuItemsByCategory(@Param("category")CategoryEntity category);

    public MenuEntriesEntity findByMenuItemAndCategory(MenuItemEntity menuItem, CategoryEntity category);
}
