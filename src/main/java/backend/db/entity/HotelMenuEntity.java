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
@Table(name = "MENU_HOTEL")
public class HotelMenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @OneToOne
    @JoinColumn(name = "REF_HOTEL")
    private HotelEntity hotel;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public HotelEntity getHotel()
    {
        return hotel;
    }

    public void setHotel(HotelEntity hotel)
    {
        this.hotel = hotel;
    }
}
