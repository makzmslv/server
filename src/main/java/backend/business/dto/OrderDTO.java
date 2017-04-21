package backend.business.dto;

import java.math.BigDecimal;

public class OrderDTO
{
    private Integer id;

    private Integer status;

    private Integer hotelId;

    private Integer tableNo;

    private String nameOfCustomer;

    private BigDecimal contactNo;

    private String address;

    public String getNameOfCustomer()
    {
        return nameOfCustomer;
    }

    public void setNameOfCustomer(String nameOfCustomer)
    {
        this.nameOfCustomer = nameOfCustomer;
    }

    public BigDecimal getContactNo()
    {
        return contactNo;
    }

    public void setContactNo(BigDecimal contactNo)
    {
        this.contactNo = contactNo;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public Integer getTableNo()
    {
        return tableNo;
    }

    public void setTableNo(Integer tableNo)
    {
        this.tableNo = tableNo;
    }

    public Integer getHotelId()
    {
        return hotelId;
    }

    public void setHotelId(Integer hotelId)
    {
        this.hotelId = hotelId;
    }
}
