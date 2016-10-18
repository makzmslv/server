package backend.business.dto;

import java.util.List;

public class MenuCategoryDTO
{
    private CategoryDTO category;
    private List<MenuItemDTO> menuItems;
    public CategoryDTO getCategory()
    {
        return category;
    }
    public void setCategory(CategoryDTO category)
    {
        this.category = category;
    }
    public List<MenuItemDTO> getMenuItems()
    {
        return menuItems;
    }
    public void setMenuItems(List<MenuItemDTO> menuItems)
    {
        this.menuItems = menuItems;
    }

}
