package backend.db.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ORDERS")
public class OrderEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "STATUS")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "REF_CUSTOMER_ACCOUNT_DETAILS")
    private CustomerDetailsEntity customerDetails;

    @ManyToOne
    @JoinColumn(name = "REF_HOTEL")
    private HotelEntity hotel;

    @OneToOne(mappedBy = "orderEntity", cascade = CascadeType.ALL)
    private BillEntity bill;

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL)
    private List<OrderDetailsEntity> orderDetails;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public CustomerDetailsEntity getCustomerDetails()
    {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetailsEntity customerDetails)
    {
        this.customerDetails = customerDetails;
    }

    public BillEntity getBill()
    {
        return bill;
    }

    public void setBill(BillEntity bill)
    {
        this.bill = bill;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

    public List<OrderDetailsEntity> getOrderDetails()
    {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailsEntity> orderDetails)
    {
        this.orderDetails = orderDetails;
    }

    public HotelEntity getHotel()
    {
        return hotel;
    }

    public void setHotel(HotelEntity hotel)
    {
        this.hotel = hotel;
    }
}
