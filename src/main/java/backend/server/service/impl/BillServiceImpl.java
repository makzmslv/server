package backend.server.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.business.dto.BillCreateDTO;
import backend.business.dto.BillDTO;
import backend.business.enums.ErrorCodes;
import backend.business.enums.OrderStatus;
import backend.business.enums.ParameterKeys;
import backend.business.error.ErrorMessage;
import backend.business.error.ServerException;
import backend.db.dao.BillDAO;
import backend.db.dao.ParameterDAO;
import backend.db.entity.BillEntity;
import backend.db.entity.OrderDetailsEntity;
import backend.db.entity.OrderEntity;

@Service
@Transactional
public class BillServiceImpl
{
    @Autowired
    private BillDAO billDAO;

    @Autowired
    private ParameterDAO parameterDAO;

    @Autowired
    private EntryExistingValidator validator;

    @Autowired
    private Mapper mapper;

    public BillDTO createBill(BillCreateDTO createDTO)
    {
        OrderEntity orderEntity = validator.getOrderEntityFromId(createDTO.getOrderId());
        validateInput(orderEntity);
        BillEntity bill = new BillEntity();
        bill.setTable(orderEntity.getTable());
        bill.setOrder(orderEntity);
        prepareBillEntity(bill);
        bill = billDAO.save(bill);
        orderEntity.setStatus(OrderStatus.BILL_GENERATED.getCode());
        return mapper.map(bill, BillDTO.class);
    }

    public BillDTO getBillForOrder(Integer orderId)
    {
        OrderEntity orderEntity = validator.getOrderEntityFromId(orderId);
        BillEntity bill = billDAO.findByOrderEntity(orderEntity);
        return mapper.map(bill, BillDTO.class);
    }

    public BillDTO recalculateBill(Integer orderId)
    {
        OrderEntity orderEntity = validator.getOrderEntityFromId(orderId);
        if (OrderStatus.ORDER_COMPLETED.getCode().equals(orderEntity.getStatus()))
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.INVALID_ORDER_STATUS));
        }
        BillEntity bill = billDAO.findByOrderEntity(orderEntity);
        prepareBillEntity(bill);
        bill = billDAO.save(bill);
        orderEntity.setStatus(OrderStatus.BILL_GENERATED.getCode());
        return mapper.map(bill, BillDTO.class);
    }

    private void validateInput(OrderEntity orderEntity)
    {
        if (!OrderStatus.ORDER_COMPLETED.getCode().equals(orderEntity.getStatus()))
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.INVALID_ORDER_STATUS));
        }
        if (OrderStatus.getBillGeneratedForOrderStatuses().contains(orderEntity.getStatus()))
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.BILL_ALREADY_EXISTS));
        }
    }

    private void prepareBillEntity(BillEntity bill)
    {
        List<OrderDetailsEntity> orderItems = bill.getOrder().getOrderDetails();
        BigDecimal billAmount = BigDecimal.ZERO;
        for (OrderDetailsEntity orderItem : orderItems)
        {
            BigDecimal costOfItem = new BigDecimal(orderItem.getCostOfItem());
            billAmount = billAmount.add(costOfItem);
        }
        String taxValue = parameterDAO.findByKey(ParameterKeys.TAX.getKey()).getValue();
        BigDecimal taxToApply = new BigDecimal(taxValue).divide(new BigDecimal(100));
        BigDecimal taxAmount = billAmount.multiply(taxToApply);
        BigDecimal totalAmount = billAmount.add(taxAmount);
        bill.setBillAmount(billAmount);
        bill.setTaxAmount(taxAmount);
        bill.setTaxApplied(taxToApply);
        bill.setTotalAmount(totalAmount);
        bill.setTimestamp(DateTime.now().toDate());
    }
}
