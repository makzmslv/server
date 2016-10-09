package backend.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.ParameterEntity;

public interface ParameterDAO extends JpaRepository<ParameterEntity, Integer>
{
    public ParameterEntity findByKey(String key);
}
