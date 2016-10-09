package backend.server.service.impl;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.business.dto.MenuItemUnitDTO;
import backend.business.enums.ErrorCodes;
import backend.business.error.ErrorMessage;
import backend.business.error.ServerException;
import backend.db.dao.MenuItemDAO;
import backend.db.dao.MenuItemUnitDAO;
import backend.db.entity.MenuItemEntity;
import backend.db.entity.MenuItemUnitEntity;

@Service
@Transactional
public class MenuItemUnitServiceImpl
{
    @Autowired
    private MenuItemUnitDAO menuItemUnitDAO;

    @Autowired
    private MenuItemDAO menuItemDAO;

    @Autowired
    private Mapper mapper;

    public MenuItemUnitDTO createMenuItemUnit(Integer menuItemId, MenuItemUnitDTO createDTO)
    {
        MenuItemEntity menuItem = getMenuItem(menuItemId);
        MenuItemUnitEntity menuItemUnit = mapper.map(createDTO, MenuItemUnitEntity.class);
        menuItemUnit.setMenuItem(menuItem);
        menuItemUnit = menuItemUnitDAO.save(menuItemUnit);
        return mapper.map(menuItemUnit, MenuItemUnitDTO.class);
    }

    public MenuItemUnitDTO updateMenuItemUnit(Integer menuItemId, Integer menuItemUnitId, MenuItemUnitDTO updateDTO)
    {
        getMenuItem(menuItemId);
        MenuItemUnitEntity menuItemUnit = getMenuItemUnit(menuItemUnitId);
        menuItemUnit = mapper.map(updateDTO, MenuItemUnitEntity.class);
        menuItemUnit = menuItemUnitDAO.save(menuItemUnit);
        return mapper.map(menuItemUnit, MenuItemUnitDTO.class);
    }

    private MenuItemEntity getMenuItem(Integer menuItemId)
    {
        MenuItemEntity menuItem = menuItemDAO.findOne(menuItemId);
        if (menuItem == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.MENU_ITEM_NOT_FOUND));
        }
        return menuItem;
    }

    private MenuItemUnitEntity getMenuItemUnit(Integer menuItemUnitId)
    {
        MenuItemUnitEntity menuItemUnit = menuItemUnitDAO.findOne(menuItemUnitId);
        if (menuItemUnit == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.MENU_ITEM_UNIT_NOT_FOUND));
        }
        return menuItemUnit;
    }
}
