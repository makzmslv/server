package backend.business.dto;

public class OrderCreateDTO
{
    private String username;

    private Integer hotelId;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
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
