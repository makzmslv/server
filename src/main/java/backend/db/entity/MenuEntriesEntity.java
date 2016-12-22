package backend.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MENU_ITEM_LIST")
public class MenuEntriesEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @OneToOne
    @JoinColumn(name = "REF_MENU_ITEM")
    private MenuItemEntity menuItem;

    @OneToOne
    @JoinColumn(name = "REF_SUB_CATEGORY")
    private SubCategoryEntity subCategory;

    @OneToOne
    @JoinColumn(name = "REF_MENU_HOTEL")
    private HotelMenuEntity hotelMenu;

    public HotelMenuEntity getHotelMenu()
    {
        return hotelMenu;
    }

    public void setHotelMenu(HotelMenuEntity hotelMenu)
    {
        this.hotelMenu = hotelMenu;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public MenuItemEntity getMenuItem()
    {
        return menuItem;
    }

    public void setMenuItem(MenuItemEntity menuItem)
    {
        this.menuItem = menuItem;
    }

    public SubCategoryEntity getSubCategory()
    {
        return subCategory;
    }

    public void setSubCategory(SubCategoryEntity subCategory)
    {
        this.subCategory = subCategory;
    }


}
