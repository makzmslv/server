package backend.business.dto;

import java.util.List;

public class MenuDTO
{
    private List<MenuCategoryDTO> categories;

    public List<MenuCategoryDTO> getCategories()
    {
        return categories;
    }

    public void setCategories(List<MenuCategoryDTO> categories)
    {
        this.categories = categories;
    }
}
