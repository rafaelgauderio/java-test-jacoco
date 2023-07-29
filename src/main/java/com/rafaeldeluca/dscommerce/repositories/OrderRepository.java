package com.rafaeldeluca.dscommerce.repositories;

import com.rafaeldeluca.dscommerce.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
