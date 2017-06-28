package backend.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOMER_ACCOUNT_DETAILS")
public class CustomerAccountDetailsEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "uNAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "VERIFIED")
    private Boolean enabled;

    @Column(name = "ROLE")
    private Integer role;

    @OneToOne
    @JoinColumn(name = "REF_CUSTOMER_DETAILS")
    private CustomerDetailsEntity customerDetails;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }

    public Integer getRole()
    {
        return role;
    }

    public void setRole(Integer role)
    {
        this.role = role;
    }

    public CustomerDetailsEntity getCustomerDetails()
    {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetailsEntity customerDetails)
    {
        this.customerDetails = customerDetails;
    }

}
