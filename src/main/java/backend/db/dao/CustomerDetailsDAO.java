package backend.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.CustomerDetailsEntity;

public interface CustomerDetailsDAO extends JpaRepository<CustomerDetailsEntity, Integer>
{
 public CustomerDetailsEntity findByRegistrationId(Integer registrationId);
 
 public CustomerDetailsEntity findByLoginDetails_UserName(String username);
}
