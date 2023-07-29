package com.rafaeldeluca.dscommerce.repositories;

import com.rafaeldeluca.dscommerce.entities.OrderItem;
import com.rafaeldeluca.dscommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
