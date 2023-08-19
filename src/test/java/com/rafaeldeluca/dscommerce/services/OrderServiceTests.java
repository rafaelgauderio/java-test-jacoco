package com.rafaeldeluca.dscommerce.services;

import com.rafaeldeluca.dscommerce.dto.OrderDTO;
import com.rafaeldeluca.dscommerce.entities.Order;
import com.rafaeldeluca.dscommerce.entities.User;
import com.rafaeldeluca.dscommerce.repositories.OrderRepository;
import com.rafaeldeluca.dscommerce.tests.OrderFactory;
import com.rafaeldeluca.dscommerce.tests.UserFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    // com injectMock tem que usar a funcao Mockito.spy() para simular os dados mockados
    @InjectMocks
    private OrderService orderService;

    private String userNameClient, userNameAdmin;
    private User userClient, userAdmin;
    private Long existingOrderId, nonExistingOrderId;
    private Order order;
    private OrderDTO orderDTO;

    void setUp ( ) {

        userNameClient = "juliana@gmail.com";
        userNameAdmin = "deluca1712@gmail.com";

        existingOrderId = 1L;
        nonExistingOrderId = 50L;

        userClient = UserFactory.createCustomUserClient(1L, userNameClient);
        userAdmin = UserFactory.createCustomAdminUser(2l, userNameAdmin);

        order = OrderFactory.createOrder(userClient);

        orderDTO = new OrderDTO(order);

        // mocando buscar pedido por Id
        Mockito.when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

    }

}
