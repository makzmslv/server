package backend.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.BillEntity;
import backend.db.entity.OrderEntity;

public interface BillDAO extends JpaRepository<BillEntity, Integer>
{
    public BillEntity findByOrderEntity(OrderEntity orderEntity);
}
