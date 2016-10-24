package backend.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.CustomerDetailsEntity;

public interface CustomerDetailsDAO extends JpaRepository<CustomerDetailsEntity, Integer>
{

}