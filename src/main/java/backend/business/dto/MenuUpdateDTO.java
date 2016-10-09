package backend.business.dto;

import javax.validation.constraints.NotNull;

public class MenuUpdateDTO
{
    @NotNull
    private Integer menuItemId;

    @NotNull
    private Integer categoryId;

    public Integer getMenuItemId()
    {
        return menuItemId;
    }

    public void setMenuItemId(Integer menuItemId)
    {
        this.menuItemId = menuItemId;
    }

    public Integer getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId)
    {
        this.categoryId = categoryId;
    }
}
