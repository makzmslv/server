package backend.db.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOMER_DETAILS")
public class CustomerDetailsEntity
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "REGISTRATION_ID")
    private Integer registrationId;

    @Column(name = "CONTACT_NO")
    private BigDecimal contactno;

    @Column(name = "LATITUDE")
    private BigDecimal latitude;

    @Column(name = "LONGITUDE")
    private BigDecimal longitude;
    
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "customerDetails", cascade = CascadeType.ALL)
    CustomerAccountDetailsEntity loginDetails;

    public CustomerAccountDetailsEntity getLoginDetails() {
		return loginDetails;
	}

	public void setLoginDetails(CustomerAccountDetailsEntity loginDetails) {
		this.loginDetails = loginDetails;
	}

	public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Integer getRegistrationId()
    {
        return registrationId;
    }

    public void setRegistrationId(Integer registrationId)
    {
        this.registrationId = registrationId;
    }

    public BigDecimal getContactno()
    {
        return contactno;
    }

    public void setContactno(BigDecimal contactno)
    {
        this.contactno = contactno;
    }

    public BigDecimal getLatitude()
    {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude)
    {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude()
    {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude)
    {
        this.longitude = longitude;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }
}
