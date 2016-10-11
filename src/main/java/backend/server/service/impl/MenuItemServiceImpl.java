package backend.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.business.dto.MenuItemCreateDTO;
import backend.business.dto.MenuItemDTO;
import backend.business.dto.MenuItemUpdateActiveStatusDTO;
import backend.business.dto.MenuItemUpdateDTO;
import backend.business.enums.ErrorCodes;
import backend.business.error.ErrorMessage;
import backend.business.error.ServerException;
import backend.business.library.UtilHelper;
import backend.db.dao.MenuItemListDAO;
import backend.db.dao.MenuItemDAO;
import backend.db.entity.MenuEntriesEntity;
import backend.db.entity.MenuItemEntity;
import backend.db.entity.MenuItemUnitEntity;

@Service
public class MenuItemServiceImpl
{
    @Autowired
    private MenuItemDAO menuItemDAO;

    @Autowired
    private MenuItemListDAO menuItemListDAO;

    @Autowired
    private EntryExistingValidator validator;

    @Autowired
    private Mapper mapper;

    public MenuItemDTO createMenuItem(MenuItemCreateDTO createDTO)
    {
        validateCreateDTO(createDTO);
        MenuItemEntity menuItem = mapper.map(createDTO, MenuItemEntity.class);
        setReferences(menuItem);
        menuItem = menuItemDAO.save(menuItem);
        return mapper.map(menuItem, MenuItemDTO.class);
    }

    public MenuItemDTO updateMenuItemDetails(Integer menuItemId, MenuItemUpdateDTO updateDTO)
    {
        MenuItemEntity menuItem = validator.getMenuItemEntityFromId(menuItemId);
        validateUpdateDTO(updateDTO, menuItem);
        mapper.map(menuItem, updateDTO);
        menuItemDAO.save(menuItem);
        return mapper.map(menuItem, MenuItemDTO.class);
    }

    public MenuItemDTO updateMenuItemActiveStatus(Integer menuItemId, MenuItemUpdateActiveStatusDTO updateDTO)
    {
        MenuItemEntity menuItem = validator.getMenuItemEntityFromId(menuItemId);
        List<MenuEntriesEntity> menuEntries = menuItemListDAO.findByMenuItem(menuItem);
        if (!menuEntries.isEmpty())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.MENU_ITEM_IN_USE));
        }
        menuItem.setActive(updateDTO.getActive());
        menuItemDAO.save(menuItem);
        return mapper.map(menuItem, MenuItemDTO.class);
    }

    public List<MenuItemDTO> findAll()
    {
        List<MenuItemEntity> menuItems = menuItemDAO.findAll();
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, menuItems, MenuItemDTO.class);
    }

    public List<MenuItemDTO> findAllUncategorizedItems(Boolean active)
    {
        List<Integer> menuItemIds = new ArrayList<Integer>();//.getMenuItemIds();
        List<MenuItemEntity> menuItems = menuItemDAO.findByActiveAndIdNotIn(active, menuItemIds);
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, menuItems, MenuItemDTO.class);
    }

    public List<MenuItemDTO> findbyActiveStatus(Boolean active)
    {
        List<MenuItemEntity> menuItems = menuItemDAO.findByActive(active);
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, menuItems, MenuItemDTO.class);
    }

    public void deleteMenuItem(Integer menuItemId)
    {
        MenuItemEntity menuItem = validator.getMenuItemEntityFromId(menuItemId);
        List<MenuEntriesEntity> itemsInMenu = menuItemListDAO.findByMenuItem(menuItem);
        for (MenuEntriesEntity item : itemsInMenu)
        {
            menuItemListDAO.delete(item);
        }
        menuItemDAO.delete(menuItem);
    }

    private void validateCreateDTO(MenuItemCreateDTO createDTO)
    {
        validateName(createDTO.getName());
        validateCode(createDTO.getCode());
    }

    private void validateUpdateDTO(MenuItemUpdateDTO updateDTO, MenuItemEntity menuItem)
    {
        if (menuItem.getName() != updateDTO.getName())
        {
            validateName(updateDTO.getName());
        }
        if (menuItem.getCode() != updateDTO.getCode())
        {
            validateCode(updateDTO.getCode());
        }
    }

    private void validateName(String name)
    {
        MenuItemEntity menuItem = menuItemDAO.findByName(name);
        if (menuItem != null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.MENU_ITEM_ALREADY_EXISTS));
        }
    }

    private void validateCode(Integer code)
    {
        MenuItemEntity menuItem = menuItemDAO.findByCode(code);
        if (menuItem != null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.DUPLICATE_CODE_FOR_MENU_ITEM));
        }
    }

    private void setReferences(MenuItemEntity menuItem)
    {
        if (menuItem.getMenuItemDetails() != null)
        {
            menuItem.getMenuItemDetails().setMenuItem(menuItem);
        }

        if (menuItem.getMenuItemUnits() != null)
        {
            for (MenuItemUnitEntity menuItemUnit : menuItem.getMenuItemUnits())
            {
                menuItemUnit.setMenuItem(menuItem);
            }
        }
    }
}
