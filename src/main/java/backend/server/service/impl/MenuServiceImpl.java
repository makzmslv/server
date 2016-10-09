package backend.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.business.dto.MenuCreateDTO;
import backend.business.dto.MenuDTO;
import backend.business.dto.MenuUpdateDTO;
import backend.business.enums.ErrorCodes;
import backend.business.error.ErrorMessage;
import backend.business.error.ServerException;
import backend.business.library.UtilHelper;
import backend.db.dao.MenuDAO;
import backend.db.entity.CategoryEntity;
import backend.db.entity.MenuEntity;
import backend.db.entity.MenuItemEntity;

@Service
@Transactional
public class MenuServiceImpl
{
    @Autowired
    private MenuDAO menuDAO;

    @Autowired
    private EntryExistingValidator validator;

    @Autowired
    private Mapper mapper;

    public List<MenuDTO> createMenuEntries(List<MenuCreateDTO> createDTOs)
    {
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
            MenuEntity menuEntity = createMenuEntryEntity(categoryEntity, menuItemEntity);
            menuEntity = menuDAO.save(menuEntity);
            menuEntries.add(mapper.map(menuEntity, MenuDTO.class));
        }
        return menuEntries;
    }

    public MenuDTO updateMenuEntry(Integer menuEntryId, MenuUpdateDTO updateDTO)
    {
        MenuEntity menuEntity = validator.getMenuEntityFromId(menuEntryId);
        checkIfFieldsAreUpdated(menuEntity, updateDTO);
        CategoryEntity categoryEntity = validator.getCategoryEntityFromId(updateDTO.getCategoryId());
        MenuItemEntity menuItemEntity = validator.getMenuItemEntityFromId(updateDTO.getMenuItemId());
        checkIfInActive(categoryEntity, menuItemEntity);
        checkIfMenuEntryAlreadyExists(categoryEntity, menuItemEntity);
        menuEntity.setCategory(categoryEntity);
        menuEntity.setMenuItem(menuItemEntity);
        menuEntity = menuDAO.save(menuEntity);
        return mapper.map(menuEntity, MenuDTO.class);
    }

    public List<MenuDTO> getMenuEntries()
    {
        List<MenuEntity> menuEntries = menuDAO.findAll();
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, menuEntries, MenuDTO.class);
    }

    public void deleteMenuEntry(Integer menuEntryId)
    {
        MenuEntity menuEntity = validator.getMenuEntityFromId(menuEntryId);
        menuDAO.delete(menuEntity);
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
        MenuEntity menuEntity = menuDAO.findByMenuItemAndCategory(menuItemEntity, categoryEntity);
        if (menuEntity != null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.MENU_ENTRY_ALREADY_EXISTS));
        }
    }

    private void checkIfFieldsAreUpdated(MenuEntity menuEntity, MenuUpdateDTO updateDTO)
    {
        if (menuEntity.getCategory().getId() == updateDTO.getCategoryId() && menuEntity.getMenuItem().getId() == updateDTO.getMenuItemId())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.NO_FIELDS_UPDATED));
        }

    }

    private MenuEntity createMenuEntryEntity(CategoryEntity categoryEntity, MenuItemEntity menuItemEntity)
    {
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setCategory(categoryEntity);
        menuEntity.setMenuItem(menuItemEntity);
        return menuEntity;
    }
}
