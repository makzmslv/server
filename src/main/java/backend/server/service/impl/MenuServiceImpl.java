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
import backend.business.enums.ErrorCodes;
import backend.business.error.ErrorMessage;
import backend.business.error.ServerException;
import backend.business.library.UtilHelper;
import backend.db.dao.HotelDAO;
import backend.db.dao.HotelMenuDAO;
import backend.db.dao.MenuItemListDAO;
import backend.db.entity.CategoryEntity;
import backend.db.entity.HotelEntity;
import backend.db.entity.HotelMenuEntity;
import backend.db.entity.MenuEntriesEntity;
import backend.db.entity.MenuItemEntity;

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
    private EntryExistingValidator validator;

    @Autowired
    private Mapper mapper;

    public List<MenuDTO> createMenuEntries(Integer hotelId, List<MenuCreateDTO> createDTOs)
    {
        HotelEntity hotel = hotelDAO.findOne(hotelId);
        if(hotel == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.HOTEL_NOT_FOUND));
        }
        HotelMenuEntity hotelMenu = hotelMenuDAO.findByHotel(hotel);
        if(hotelMenu == null)
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
            CategoryEntity categoryEntity = validator.getCategoryEntityFromId(menuItem.getCategoryId());
            MenuItemEntity menuItemEntity = validator.getMenuItemEntityFromId(menuItem.getMenuItemId());
            checkIfInActive(categoryEntity, menuItemEntity);
            checkIfMenuEntryAlreadyExists(categoryEntity, menuItemEntity);
            MenuEntriesEntity menuEntriesEntity = createMenuEntryEntity(hotelMenu, categoryEntity, menuItemEntity);
            menuEntriesEntity = menuItemListDAO.save(menuEntriesEntity);
        }
        return menuEntries;
    }

    public MenuDTO updateMenuEntry(Integer menuEntryId, MenuUpdateDTO updateDTO)
    {
        MenuEntriesEntity menuEntriesEntity = validator.getMenuEntityFromId(menuEntryId);
        checkIfFieldsAreUpdated(menuEntriesEntity, updateDTO);
        CategoryEntity categoryEntity = validator.getCategoryEntityFromId(updateDTO.getCategoryId());
        MenuItemEntity menuItemEntity = validator.getMenuItemEntityFromId(updateDTO.getMenuItemId());
        checkIfInActive(categoryEntity, menuItemEntity);
        checkIfMenuEntryAlreadyExists(categoryEntity, menuItemEntity);
        menuEntriesEntity.setCategory(categoryEntity);
        menuEntriesEntity.setMenuItem(menuItemEntity);
        menuEntriesEntity = menuItemListDAO.save(menuEntriesEntity);
        return mapper.map(menuEntriesEntity, MenuDTO.class);
    }

    public MenuDTO getMenuEntriesForHotel(Integer hotelId)
    {
        HotelEntity hotel = hotelDAO.findOne(hotelId);
        if(hotel == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.HOTEL_NOT_FOUND));
        }
        HotelMenuEntity hotelMenu = hotelMenuDAO.findByHotel(hotel);
        if(hotelMenu == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.HOTEL_MENU_ENTRY_NOT_FOUND));
        }
        List<MenuCategoryDTO> menuCategories = new ArrayList<MenuCategoryDTO>();
        List<MenuEntriesEntity> menuEntries = menuItemListDAO.findByHotelMenuOrderByCategoryDisplayRankAsc(hotelMenu);
        for (MenuEntriesEntity menuEntriesEntity : menuEntries)
        {
            MenuCategoryDTO menuCategory =  new MenuCategoryDTO();
            menuCategory.setCategory(mapper.map(menuEntriesEntity.getCategory(), CategoryDTO.class));
            menuCategory.setMenuItems(UtilHelper.mapListOfEnitiesToDTOs(mapper, menuItemListDAO.getMenuItemsByCategory(menuEntriesEntity.getCategory()), MenuItemDTO.class));
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

    private void checkIfInActive(CategoryEntity categoryEntity, MenuItemEntity menuItemEntity)
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

    private void checkIfMenuEntryAlreadyExists(CategoryEntity categoryEntity, MenuItemEntity menuItemEntity)
    {
        MenuEntriesEntity menuEntriesEntity = menuItemListDAO.findByMenuItemAndCategory(menuItemEntity, categoryEntity);
        if (menuEntriesEntity != null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.MENU_ENTRY_ALREADY_EXISTS));
        }
    }

    private void checkIfFieldsAreUpdated(MenuEntriesEntity menuEntriesEntity, MenuUpdateDTO updateDTO)
    {
        if (menuEntriesEntity.getCategory().getId() == updateDTO.getCategoryId() && menuEntriesEntity.getMenuItem().getId() == updateDTO.getMenuItemId())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.NO_FIELDS_UPDATED));
        }

    }

    private MenuEntriesEntity createMenuEntryEntity(HotelMenuEntity hotelMenu, CategoryEntity categoryEntity, MenuItemEntity menuItemEntity)
    {
        MenuEntriesEntity menuEntriesEntity = new MenuEntriesEntity();
        menuEntriesEntity.setCategory(categoryEntity);
        menuEntriesEntity.setMenuItem(menuItemEntity);
        menuEntriesEntity.setHotelMenu(hotelMenu);
        return menuEntriesEntity;
    }
}
