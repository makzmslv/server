package backend.business.enums;

public enum UserAuthorities
{
    ADMIN(1),
    USER(2),
    NOT_VERIFIED(3);

    private Integer role;

    UserAuthorities(Integer role)
    {
        this.role = role;
    }

    public Integer getRole()
    {
        return role;
    }

}
