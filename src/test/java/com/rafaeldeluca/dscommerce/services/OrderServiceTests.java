package com.rafaeldeluca.dscommerce.services;

import com.rafaeldeluca.dscommerce.dto.OrderDTO;
import com.rafaeldeluca.dscommerce.entities.Order;
import com.rafaeldeluca.dscommerce.entities.User;
import com.rafaeldeluca.dscommerce.repositories.OrderRepository;
import com.rafaeldeluca.dscommerce.services.exceptions.ForbiddenException;
import com.rafaeldeluca.dscommerce.tests.OrderFactory;
import com.rafaeldeluca.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AuthService authService;

    // com injectMock tem que usar a funcao Mockito.spy() para simular os dados mockados
    @InjectMocks
    private OrderService orderService;

    private String userNameClient, userNameAdmin;
    private User userClient, userAdmin;
    private Long existingOrderId, nonExistingOrderId;
    private Order order;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp () throws Exception {

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

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndUserLoggedAsAdmin () {
        // admin pode consultar o pedido de todos os usuários
        //Mockito.doNothing().when(authService).validateSelfOrAdmin(userAdmin.getId());
        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

        OrderDTO orderDTO = orderService.findById(existingOrderId);

        Assertions.assertNotNull(orderDTO);
        Assertions.assertEquals(orderDTO.getId(), existingOrderId);
    }

    @Test
    public void findByIdShouldReturnThrowsForbiddenExceptionWhenIdExistsAndUerLoggedAsClientTryToAccessNotSelfOrder () {
        // usuario logado como cliente não pode acessar os pedidos de outros usuários
        Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(userClient.getId());

        Assertions.assertThrows(ForbiddenException.class, () -> {
           OrderDTO orderDTO = orderService.findById(existingOrderId);
        });
    }

}