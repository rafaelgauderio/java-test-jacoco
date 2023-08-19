package com.rafaeldeluca.dscommerce.tests;

import com.rafaeldeluca.dscommerce.entities.*;

import java.time.Instant;

public class OrderFactory {

    public static Order createOrder (User userClient) {
        Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, userClient, new Payment());

        Product product = ProductFactory.createProduct("TV Sansung");
        Product product2 = ProductFactory.createProduct("Tablet Apple");
        // associacao entre pedido e produto
        OrderItem orderItem = new OrderItem(order, product, 2, 2000.50);
        OrderItem orderItem2 = new OrderItem(order, product, 1, 3000.50);
        order.getItems().add(orderItem);
        order.getItems().add(orderItem2);

        return order;
    }
}