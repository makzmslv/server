package backend.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.business.dto.CategoryDTO;
import backend.business.dto.MenuCategoryDTO;
import backend.business.dto.MenuCreateDTO;
import backend.business.dto.MenuDTO;
import backend.business.dto.MenuItemDTO;
import backend.business.dto.MenuUpdateDTO;
import backend.business.dto.SubCategoryDTO;
import backend.business.enums.CategorySubType;
import backend.business.enums.CategoryType;
import backend.business.enums.ErrorCodes;
import backend.business.error.ErrorMessage;
import backend.business.error.ServerException;
import backend.business.library.UtilHelper;
import backend.db.dao.CategoryDAO;
import backend.db.dao.HotelDAO;
import backend.db.dao.HotelMenuDAO;
import backend.db.dao.MenuItemListDAO;
import backend.db.dao.SubCategoryDAO;
import backend.db.entity.CategoryEntity;
import backend.db.entity.HotelEntity;
import backend.db.entity.HotelMenuEntity;
import backend.db.entity.MenuEntriesEntity;
import backend.db.entity.MenuItemEntity;
import backend.db.entity.SubCategoryEntity;

@Service
@Transactional
public class MenuServiceImpl
{
    @Autowired
    private MenuItemListDAO menuItemListDAO;

    @Autowired
    private HotelMenuDAO hotelMenuDAO;

    @Autowired
    private HotelDAO hotelDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private SubCategoryDAO subCategoryDAO;

    @Autowired
    private EntryExistingValidator validator;

    @Autowired
    private Mapper mapper;

    public List<MenuDTO> createMenuEntries(Integer hotelId, List<MenuCreateDTO> createDTOs)
    {
        HotelEntity hotel = hotelDAO.findOne(hotelId);
        if (hotel == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.HOTEL_NOT_FOUND));
        }
        HotelMenuEntity hotelMenu = hotelMenuDAO.findByHotel(hotel);
        if (hotelMenu == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.HOTEL_MENU_ENTRY_NOT_FOUND));
        }
        if (createDTOs.isEmpty())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.NO_FIELDS_UPDATED));
        }
        List<MenuDTO> menuEntries = new ArrayList<MenuDTO>();
        for (MenuCreateDTO menuItem : createDTOs)
        {
            SubCategoryEntity subCategoryEntity = validator.getSubCategoryEntityFromId(menuItem.getCategoryId());
            MenuItemEntity menuItemEntity = validator.getMenuItemEntityFromId(menuItem.getMenuItemId());
            checkIfInActive(subCategoryEntity, menuItemEntity);
            checkIfMenuEntryAlreadyExists(subCategoryEntity, menuItemEntity);
            MenuEntriesEntity menuEntriesEntity = createMenuEntryEntity(hotelMenu, subCategoryEntity, menuItemEntity);
            menuEntriesEntity = menuItemListDAO.save(menuEntriesEntity);
        }
        return menuEntries;
    }

    public MenuDTO updateMenuEntry(Integer menuEntryId, MenuUpdateDTO updateDTO)
    {
        MenuEntriesEntity menuEntriesEntity = validator.getMenuEntityFromId(menuEntryId);
        checkIfFieldsAreUpdated(menuEntriesEntity, updateDTO);
        SubCategoryEntity categoryEntity = validator.getSubCategoryEntityFromId(updateDTO.getCategoryId());
        MenuItemEntity menuItemEntity = validator.getMenuItemEntityFromId(updateDTO.getMenuItemId());
        checkIfInActive(categoryEntity, menuItemEntity);
        checkIfMenuEntryAlreadyExists(categoryEntity, menuItemEntity);
        menuEntriesEntity.setSubCategory(categoryEntity);
        menuEntriesEntity.setMenuItem(menuItemEntity);
        menuEntriesEntity = menuItemListDAO.save(menuEntriesEntity);
        return mapper.map(menuEntriesEntity, MenuDTO.class);
    }

    public List<CategoryDTO> getCategoriesForHotel(Integer hotelId)
    {
        HotelEntity hotel = hotelDAO.findOne(hotelId);
        if (hotel == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.HOTEL_NOT_FOUND));
        }
        HotelMenuEntity hotelMenu = hotelMenuDAO.findByHotel(hotel);
        if (hotelMenu == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.HOTEL_MENU_ENTRY_NOT_FOUND));
        }
        List<CategoryEntity> categories = categoryDAO.findByHotel(hotel);
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, categories, CategoryDTO.class);
    }

    public List<SubCategoryDTO> getSubCatgoeriesForHotel(Integer categoryId, Integer subType)
    {
        CategoryEntity category = categoryDAO.findOne(categoryId);
        if (category == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_NOT_FOUND));
        }
        List<SubCategoryEntity> subCategories = subCategoryDAO.findByCategoryAndTypeAndSubType(category, CategoryType.FOOD.getCode(), subType);
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, subCategories, SubCategoryDTO.class);
    }

    public List<MenuItemDTO> getMenuItemsForSubCategory(Integer subCategoryId)
    {
        SubCategoryEntity subcategory = subCategoryDAO.findOne(subCategoryId);
        if (subcategory == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_NOT_FOUND));
        }
        List<MenuEntriesEntity> menuEntries = menuItemListDAO.findBySubCategory(subcategory);
        List<MenuItemEntity> menuItems = new ArrayList<MenuItemEntity>();
        for (MenuEntriesEntity menuEntriesEntity : menuEntries)
        {
            menuItems.add(menuEntriesEntity.getMenuItem());
        }
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, menuItems, MenuItemDTO.class);
    }

    public MenuDTO getMenuEntriesForHotel(Integer hotelId)
    {
        HotelEntity hotel = hotelDAO.findOne(hotelId);
        if (hotel == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.HOTEL_NOT_FOUND));
        }
        HotelMenuEntity hotelMenu = hotelMenuDAO.findByHotel(hotel);
        if (hotelMenu == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.HOTEL_MENU_ENTRY_NOT_FOUND));
        }
        List<MenuCategoryDTO> menuCategories = new ArrayList<MenuCategoryDTO>();
        List<CategoryEntity> categories = categoryDAO.findByHotel(hotel);
        for (CategoryEntity categoryEntity : categories)
        {
            MenuCategoryDTO menuCategory = new MenuCategoryDTO();
            menuCategory.setCategory(mapper.map(categoryEntity, CategoryDTO.class));
            List<MenuEntriesEntity> menuEntries = menuItemListDAO.findBySubCategoryCategoryAndSubCategorySubType(categoryEntity, CategorySubType.VEG.getCode());
            List<MenuItemEntity> menuItems = new ArrayList<MenuItemEntity>();
            for (MenuEntriesEntity menuEntriesEntity : menuEntries)
            {
                menuItems.add(menuEntriesEntity.getMenuItem());
            }
            menuCategory.setVegMenuItems(UtilHelper.mapListOfEnitiesToDTOs(mapper, menuItems, MenuItemDTO.class));
            menuEntries.clear();
            menuItems.clear();
            menuEntries = menuItemListDAO.findBySubCategoryCategoryAndSubCategorySubType(categoryEntity, CategorySubType.NON_VEG.getCode());
            for (MenuEntriesEntity menuEntriesEntity : menuEntries)
            {
                menuItems.add(menuEntriesEntity.getMenuItem());
            }
            menuCategory.setNonVegmenuItems(UtilHelper.mapListOfEnitiesToDTOs(mapper, menuItems, MenuItemDTO.class));
            menuCategories.add(menuCategory);
        }
        MenuDTO menu = new MenuDTO();
        menu.setCategories(menuCategories);
        return menu;
    }

    public void deleteMenuEntry(Integer menuEntryId)
    {
        MenuEntriesEntity menuEntriesEntity = validator.getMenuEntityFromId(menuEntryId);
        menuItemListDAO.delete(menuEntriesEntity);
    }

    private void checkIfInActive(SubCategoryEntity categoryEntity, MenuItemEntity menuItemEntity)
    {
        if (!categoryEntity.getActive())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_INACTIVE));
        }
        if (!menuItemEntity.getActive())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.MENU_ITEM_INACTIVE));
        }
    }

    private void checkIfMenuEntryAlreadyExists(SubCategoryEntity categoryEntity, MenuItemEntity menuItemEntity)
    {
        MenuEntriesEntity menuEntriesEntity = menuItemListDAO.findByMenuItemAndSubCategory(menuItemEntity, categoryEntity);
        if (menuEntriesEntity != null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.MENU_ENTRY_ALREADY_EXISTS));
        }
    }

    private void checkIfFieldsAreUpdated(MenuEntriesEntity menuEntriesEntity, MenuUpdateDTO updateDTO)
    {
        if (menuEntriesEntity.getSubCategory().getId() == updateDTO.getCategoryId() && menuEntriesEntity.getMenuItem().getId() == updateDTO.getMenuItemId())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.NO_FIELDS_UPDATED));
        }

    }

    private MenuEntriesEntity createMenuEntryEntity(HotelMenuEntity hotelMenu, SubCategoryEntity categoryEntity, MenuItemEntity menuItemEntity)
    {
        MenuEntriesEntity menuEntriesEntity = new MenuEntriesEntity();
        menuEntriesEntity.setSubCategory(categoryEntity);
        menuEntriesEntity.setMenuItem(menuItemEntity);
        menuEntriesEntity.setHotelMenu(hotelMenu);
        return menuEntriesEntity;
    }
}
