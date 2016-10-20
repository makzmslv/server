package backend.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.CustomerDetailsEntity;
import backend.db.entity.OrderEntity;

public interface OrderDAO extends JpaRepository<OrderEntity, Integer>
{
    public OrderEntity findByCustomerDetailsAndStatusNot(CustomerDetailsEntity customerAccDetails, Integer status);

    public List<OrderEntity> findByCustomerDetailsOrderByIdDesc(CustomerDetailsEntity customerAccDetails);
}
