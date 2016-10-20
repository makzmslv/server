package backend.db.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BILL")
public class BillEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "REF_CUSTOMER_ACCOUNT_DETAILS")
    private CustomerDetailsEntity customerDetails;

    @OneToOne
    @JoinColumn(name = "REF_ORDER")
    private OrderEntity orderEntity;

    @Column(name = "BILL_AMOUNT")
    private BigDecimal billAmount;

    @Column(name = "TAX_APPLIED")
    private BigDecimal taxApplied;

    @Column(name = "TAX_AMOUNT")
    private BigDecimal taxAmount;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public CustomerDetailsEntity getCustomerDetails()
    {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetailsEntity customerDetails)
    {
        this.customerDetails = customerDetails;
    }

    public OrderEntity getOrderEntity()
    {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity)
    {
        this.orderEntity = orderEntity;
    }

    public OrderEntity getOrder()
    {
        return orderEntity;
    }

    public void setOrder(OrderEntity order)
    {
        this.orderEntity = order;
    }

    public BigDecimal getBillAmount()
    {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount)
    {
        this.billAmount = billAmount;
    }

    public BigDecimal getTaxApplied()
    {
        return taxApplied;
    }

    public void setTaxApplied(BigDecimal taxApplied)
    {
        this.taxApplied = taxApplied;
    }

    public BigDecimal getTaxAmount()
    {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount)
    {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

}
