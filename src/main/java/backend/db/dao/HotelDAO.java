package backend.db.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.HotelEntity;

public interface HotelDAO extends JpaRepository<HotelEntity, Integer>
{
    public List<HotelEntity> findByAreaIgnoreCase(String area, Sort sort);

    public List<HotelEntity> findByNameStartsWithIgnoreCase(String area, Sort sort);
}
