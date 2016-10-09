package backend.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.OrderDetailsEntity;
import backend.db.entity.OrderEntity;

public interface OrderDetailsDAO extends JpaRepository<OrderDetailsEntity, Integer>
{
    List<OrderDetailsEntity> findByOrderEntity(OrderEntity orderEntity);

    List<OrderDetailsEntity> findByOrderEntityAndStatusNotIn(OrderEntity orderEntity, List<Integer> orderStatus);
}
