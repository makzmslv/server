package backend.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.HotelEntity;

public interface HotelDAO extends JpaRepository<HotelEntity, Integer>
{

}
