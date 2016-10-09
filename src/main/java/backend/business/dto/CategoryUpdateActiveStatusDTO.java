package backend.business.dto;

import javax.validation.constraints.NotNull;

public class CategoryUpdateActiveStatusDTO
{
    @NotNull
    private boolean active;

    public boolean getActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }
}
