package backend.business.dto;

import java.util.List;

public class MenuCategoryDTO
{
    private CategoryDTO category;
    private List<MenuItemDTO> vegMenuItems;
    private List<MenuItemDTO> nonVegmenuItems;
    public CategoryDTO getCategory()
    {
        return category;
    }
    public void setCategory(CategoryDTO category)
    {
        this.category = category;
    }
    public List<MenuItemDTO> getVegMenuItems()
    {
        return vegMenuItems;
    }
    public void setVegMenuItems(List<MenuItemDTO> vegMenuItems)
    {
        this.vegMenuItems = vegMenuItems;
    }
    public List<MenuItemDTO> getNonVegmenuItems()
    {
        return nonVegmenuItems;
    }
    public void setNonVegmenuItems(List<MenuItemDTO> nonVegmenuItems)
    {
        this.nonVegmenuItems = nonVegmenuItems;
    }

}
