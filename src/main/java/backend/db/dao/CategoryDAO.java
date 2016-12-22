package backend.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.CategoryEntity;
import backend.db.entity.HotelEntity;

public interface CategoryDAO extends JpaRepository<CategoryEntity, Integer>
{
    public CategoryEntity findByName(String name);

    public List<CategoryEntity> findByHotel(HotelEntity hotel);

}
