package backend.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.HotelMenuEntity;

public interface HotelMenuDAO extends JpaRepository<HotelMenuEntity, Integer>
{

}
