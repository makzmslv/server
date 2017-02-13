package backend.business.dto;

import javax.validation.constraints.NotNull;

public class OrderDetailsUpdateDTO
{
    @NotNull
    private Integer orderDetailsId;

    @NotNull
    private Integer status;

    @NotNull
    private Integer quantity;

    public Integer getOrderDetailsId()
    {
        return orderDetailsId;
    }

    public void setOrderDetailsId(Integer orderDetailsId)
    {
        this.orderDetailsId = orderDetailsId;
    }

    public Integer getOrderDetailsQuantity()
    {
        return quantity;
    }

    public void setOrderDetailsQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

}
