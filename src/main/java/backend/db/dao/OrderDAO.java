package backend.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.db.entity.OrderEntity;
import backend.db.entity.TableEntity;

public interface OrderDAO extends JpaRepository<OrderEntity, Integer>
{
    public OrderEntity findByTableEntityAndStatusNot(TableEntity tableEntity, Integer status);

    public List<OrderEntity> findByTableEntityOrderByIdDesc(TableEntity tableEntity);
}
