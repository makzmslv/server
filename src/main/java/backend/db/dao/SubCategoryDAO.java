package backend.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.CategoryEntity;
import backend.db.entity.SubCategoryEntity;

public interface SubCategoryDAO extends JpaRepository<SubCategoryEntity, Integer>
{
    public List<SubCategoryEntity> findByCategoryAndTypeAndSubType(CategoryEntity category, Integer type, Integer subType);

    public SubCategoryEntity findByCategoryAndDisplayRank(CategoryEntity category, Integer displayRank);
}
