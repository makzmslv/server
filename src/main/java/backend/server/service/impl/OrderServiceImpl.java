package backend.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.business.dto.OrderCreateDTO;
import backend.business.dto.OrderDTO;
import backend.business.dto.OrderDetailsCreateDTO;
import backend.business.dto.OrderDetailsDTO;
import backend.business.dto.OrderDetailsUpdateDTO;
import backend.business.dto.OrderUpdateDTO;
import backend.business.enums.ErrorCodes;
import backend.business.enums.OrderItemStatus;
import backend.business.enums.OrderStatus;
import backend.business.error.ErrorMessage;
import backend.business.error.ServerException;
import backend.business.library.UtilHelper;
import backend.db.dao.BillDAO;
import backend.db.dao.OrderDAO;
import backend.db.dao.OrderDetailsDAO;
import backend.db.entity.BillEntity;
import backend.db.entity.MenuItemEntity;
import backend.db.entity.OrderDetailsEntity;
import backend.db.entity.OrderEntity;
import backend.db.entity.TableEntity;

@Service
@Transactional
public class OrderServiceImpl
{
    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderDetailsDAO orderDetailsDAO;

    @Autowired
    private BillDAO billDAO;

    @Autowired
    private EntryExistingValidator validator;

    @Autowired
    private Mapper mapper;

    public OrderDTO createOrder(OrderCreateDTO createDTO)
    {
        TableEntity tableEntity = validator.getTableEntityFromTableNo(createDTO.getTableNo());
        if (!tableEntity.getActive())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.TABLE_INACTIVE));
        }
        checkIfOrderAlreadyInProgressForTable(tableEntity);
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setStatus(OrderStatus.CREATED.getCode());
        orderEntity.setTable(tableEntity);
        orderEntity = orderDAO.save(orderEntity);
        return mapper.map(orderEntity, OrderDTO.class);
    }

    public OrderDTO getOrder(Integer orderId)
    {
        OrderEntity orderEntity = validator.getOrderEntityFromId(orderId);
        return mapper.map(orderEntity, OrderDTO.class);
    }

    public OrderDTO updateOrderStatus(Integer orderId, OrderUpdateDTO updateDTO)
    {
        OrderEntity orderEntity = validator.getOrderEntityFromId(orderId);
        checkIfOrderStatusCanBeUpdated(updateDTO, orderEntity);
        orderEntity.setStatus(updateDTO.getStatus());
        orderEntity = orderDAO.save(orderEntity);
        return mapper.map(orderEntity, OrderDTO.class);
    }

    public List<OrderDetailsDTO> addMenuItemsToOrder(Integer orderId, List<OrderDetailsCreateDTO> orderDetailscreateDTO)
    {
        if (orderDetailscreateDTO.isEmpty())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.NO_FIELDS_UPDATED));
        }
        OrderEntity orderEntity = validator.getOrderEntityFromId(orderId);
        checkIfOrderAlreadyCompleted(orderEntity);
        List<OrderDetailsDTO> orderDetailsDTOs = new ArrayList<OrderDetailsDTO>();
        for (OrderDetailsCreateDTO orderItem : orderDetailscreateDTO)
        {
            MenuItemEntity menuItemEntity = validator.getMenuItemEntityFromId(orderItem.getMenuItemId());
            Integer cost = getCostOfMenuItem(menuItemEntity.getPrice(), orderItem.getQuantity());
            OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
            orderDetailsEntity.setCostOfItem(cost);
            orderDetailsEntity.setMenuItem(menuItemEntity);
            orderDetailsEntity.setOrder(orderEntity);
            orderDetailsEntity.setQuantity(orderItem.getQuantity());
            orderDetailsEntity.setTimestamp(DateTime.now().toDate());
            orderDetailsEntity.setStatus(OrderItemStatus.PLACED.getCode());
            orderDetailsEntity = orderDetailsDAO.save(orderDetailsEntity);
            orderDetailsDTOs.add(mapper.map(orderDetailsEntity, OrderDetailsDTO.class));
        }
        updateOrderStatus(orderEntity);
        return orderDetailsDTOs;
    }

    public List<OrderDetailsDTO> updateOrderItems(Integer orderId, List<OrderDetailsUpdateDTO> orderDetailsUpdateDTO)
    {
        if (orderDetailsUpdateDTO.isEmpty())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.NO_FIELDS_UPDATED));
        }
        OrderEntity orderEntity = validator.getOrderEntityFromId(orderId);
        checkIfOrderAlreadyCompleted(orderEntity);
        List<OrderDetailsDTO> orderDetailsDTOs = new ArrayList<OrderDetailsDTO>();
        for (OrderDetailsUpdateDTO orderItem : orderDetailsUpdateDTO)
        {
            OrderDetailsEntity orderDetailsEntity = validator.getOrderDetailsEntityId(orderItem.getOrderDetailsId());
            checkIfOrderItemCanBeUpdated(orderDetailsEntity, orderItem.getStatus());
            orderDetailsEntity.setStatus(orderItem.getStatus());
            orderDetailsEntity = orderDetailsDAO.save(orderDetailsEntity);
            orderDetailsDTOs.add(mapper.map(orderDetailsEntity, OrderDetailsDTO.class));
        }
        return orderDetailsDTOs;
    }

    public List<OrderDetailsDTO> getOrderItems(Integer orderId)
    {
        OrderEntity orderEntity = validator.getOrderEntityFromId(orderId);
        List<OrderDetailsEntity> orderItems = orderDetailsDAO.findByOrderEntity(orderEntity);
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, orderItems, OrderDetailsDTO.class);
    }

    private void checkIfOrderAlreadyInProgressForTable(TableEntity tableEntity)
    {
        OrderEntity orderEntity = orderDAO.findByTableEntityAndStatusNot(tableEntity, OrderStatus.BILL_PAID.getCode());
        if (orderEntity != null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.ORDER_IN_PROGRESS));
        }
    }

    private void checkIfOrderAlreadyCompleted(OrderEntity orderEntity)
    {
        if (orderEntity.getStatus().equals(OrderStatus.BILL_PAID))
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.ORDER_COMPLETED));
        }
    }

    private void checkIfOrderStatusCanBeUpdated(OrderUpdateDTO updateDTO, OrderEntity orderEntity)
    {
        if (OrderStatus.getOrderInProgresStatuses().contains(updateDTO.getStatus()))
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.INVALID_ORDER_STATUS));
        }
        if (OrderStatus.ORDER_COMPLETED.getCode().equals(updateDTO.getStatus()))
        {
            List<OrderDetailsEntity> orderItems = orderDetailsDAO.findByOrderEntityAndStatusNotIn(orderEntity, OrderItemStatus.getOrderInProgresStatuses());
            if (!orderItems.isEmpty())
            {
                throw new ServerException(new ErrorMessage(ErrorCodes.ORDER_IN_PROGRESS));
            }
        }
        if (OrderStatus.getBillGeneratedForOrderStatuses().contains(updateDTO.getStatus()))
        {
            BillEntity bill = billDAO.findByOrderEntity(orderEntity);
            if (bill == null)
            {
                throw new ServerException(new ErrorMessage(ErrorCodes.INVALID_ORDER_STATUS));
            }
        }
    }

    private void checkIfOrderItemCanBeUpdated(OrderDetailsEntity orderDetailsEntity, Integer orderStatus)
    {
        if (OrderItemStatus.CANCELLED.getCode().equals(orderDetailsEntity.getStatus()))
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.ORDER_ITEM_CANNOT_UPDATED_AS_ALREADY_CANCELLED));
        }
        if (OrderItemStatus.DELIVERED.getCode().equals(orderDetailsEntity.getStatus()))
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.ORDER_ITEM_CANNOT_UPDATED_AS_ALREADY_DELIVERED));
        }
        if (OrderItemStatus.CANCELLED.getCode().equals(orderStatus))
        {
            DateTime timeOfOrder = new DateTime(orderDetailsEntity.getTimestamp());
            DateTime currentTime = DateTime.now();
            DateTime timeAfterWhichOrderCannotBeCancelled = timeOfOrder.plusMinutes(5);
            if (currentTime.isAfter(timeAfterWhichOrderCannotBeCancelled))
            {
                throw new ServerException(new ErrorMessage(ErrorCodes.ORDER_ITEM_CANNOT_UPDATED_DUE_TO_TIME_ELAPSED));
            }
        }
    }

    private void updateOrderStatus(OrderEntity orderEntity)
    {
        orderEntity.setStatus(OrderStatus.ITEMS_ORDERED.getCode());
        orderDAO.save(orderEntity);
    }

    private Integer getCostOfMenuItem(Integer price, Integer quantity)
    {
        return price * quantity;
    }
}
