package backend.business.dto;

public class SubCategoryDTO
{
    private Integer subCategoryId;

    private String name;

    private Integer type;

    private Integer subType;

    private String description;

    private Integer displayRank;

    private Boolean active;

    public Integer getSubCategoryId()
    {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId)
    {
        this.subCategoryId = subCategoryId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getSubType()
    {
        return subType;
    }

    public void setSubType(Integer subType)
    {
        this.subType = subType;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Integer getDisplayRank()
    {
        return displayRank;
    }

    public void setDisplayRank(Integer displayRank)
    {
        this.displayRank = displayRank;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }
}
