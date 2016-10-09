package backend.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.CategoryEntity;

public interface CategoryDAO extends JpaRepository<CategoryEntity, Integer>
{
    public CategoryEntity findByNameAndTypeAndSubType(String name, Integer type, Integer subType);

    public CategoryEntity findByDisplayRank(Integer displayOrder);

    public List<CategoryEntity> findByActive(Boolean active);
}
