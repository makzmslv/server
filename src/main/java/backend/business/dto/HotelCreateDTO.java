package backend.business.dto;

import java.math.BigDecimal;

public class HotelCreateDTO
{
    private String name;

    private String address;

    private String area;

    private BigDecimal contactNo;

    private BigDecimal cost;

    private BigDecimal latitude;

    private BigDecimal longitude;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getArea()
    {
        return area;
    }

    public void setArea(String area)
    {
        this.area = area;
    }

    public BigDecimal getContactNo()
    {
        return contactNo;
    }

    public void setContactNo(BigDecimal contactNo)
    {
        this.contactNo = contactNo;
    }

    public BigDecimal getCost()
    {
        return cost;
    }

    public void setCost(BigDecimal cost)
    {
        this.cost = cost;
    }

    public BigDecimal getLatitude()
    {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude)
    {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude()
    {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude)
    {
        this.longitude = longitude;
    }

}
