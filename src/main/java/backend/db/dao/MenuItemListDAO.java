package backend.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import backend.db.entity.CategoryEntity;
import backend.db.entity.HotelMenuEntity;
import backend.db.entity.MenuEntriesEntity;
import backend.db.entity.MenuItemEntity;
import backend.db.entity.SubCategoryEntity;

public interface MenuItemListDAO extends JpaRepository<MenuEntriesEntity, Integer>
{
    public List<MenuEntriesEntity> findByMenuItem(MenuItemEntity menuItem);

    public List<MenuEntriesEntity> findBySubCategory(SubCategoryEntity category);

    public List<MenuEntriesEntity> findBySubCategoryCategoryAndSubCategorySubType(CategoryEntity category, Integer type);

    @Query(value = "SELECT menuEntry.menuItem.id FROM MenuEntriesEntity menuEntry WHERE menuEntry.id IS NOT NULL")
    public List<Integer> getMenuItemIds();

    @Query(value = "SELECT menuEntry.menuItem FROM MenuEntriesEntity menuEntry WHERE menuEntry IN :list")
    public List<MenuItemEntity> getMenuItemsByCategory(@Param("list")List<MenuEntriesEntity> list);

    public MenuEntriesEntity findByMenuItemAndSubCategory(MenuItemEntity menuItem, SubCategoryEntity category);
}
