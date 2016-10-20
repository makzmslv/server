package backend.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.CustomerAccountDetailsEntity;

public interface CustomerAccountDetailsDAO extends JpaRepository<CustomerAccountDetailsEntity, Integer>
{
    public CustomerAccountDetailsEntity findByUserName(String username);
}
