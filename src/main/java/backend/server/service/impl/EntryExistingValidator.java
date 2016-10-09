package backend.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.business.enums.ErrorCodes;
import backend.business.error.ErrorMessage;
import backend.business.error.ServerException;
import backend.db.dao.CategoryDAO;
import backend.db.dao.MenuDAO;
import backend.db.dao.MenuItemDAO;
import backend.db.dao.OrderDAO;
import backend.db.dao.OrderDetailsDAO;
import backend.db.dao.TableDAO;
import backend.db.entity.CategoryEntity;
import backend.db.entity.MenuEntity;
import backend.db.entity.MenuItemEntity;
import backend.db.entity.OrderDetailsEntity;
import backend.db.entity.OrderEntity;
import backend.db.entity.TableEntity;

@Service
public class EntryExistingValidator
{
    @Autowired
    private TableDAO tableDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private MenuDAO menuDAO;

    @Autowired
    private MenuItemDAO menuItemDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderDetailsDAO orderDetailsDAO;

    public CategoryEntity getCategoryEntityFromId(Integer categoryId)
    {
        CategoryEntity category = categoryDAO.findOne(categoryId);
        if (category == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_NOT_FOUND));
        }
        return category;
    }

    public MenuItemEntity getMenuItemEntityFromId(Integer menuItemId)
    {
        MenuItemEntity menuItem = menuItemDAO.findOne(menuItemId);
        if (menuItem == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.MENU_ITEM_NOT_FOUND));
        }
        return menuItem;
    }

    public MenuEntity getMenuEntityFromId(Integer menuEntryId)
    {
        MenuEntity menuEntry = menuDAO.findOne(menuEntryId);
        if (menuEntry == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.MENU_ENTRY_NOT_FOUND));
        }
        return menuEntry;
    }

    public TableEntity getTableEntityFromTableNo(Integer tableNo)
    {
        TableEntity tableEntity = tableDAO.findByTableNo(tableNo);
        if (tableEntity == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.TABLE_NOT_FOUND));
        }
        return tableEntity;
    }

    public OrderEntity getOrderEntityFromId(Integer orderId)
    {
        OrderEntity orderEntity = orderDAO.findOne(orderId);
        if (orderEntity == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.ORDER_DOES_NOT_EXIST));
        }
        return orderEntity;
    }

    public OrderDetailsEntity getOrderDetailsEntityId(Integer orderDetailsId)
    {
        OrderDetailsEntity orderDetailsEntity = orderDetailsDAO.findOne(orderDetailsId);
        if (orderDetailsEntity == null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.ORDER_DOES_NOT_EXIST));
        }
        return orderDetailsEntity;
    }
}
