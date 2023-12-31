package com.rafaeldeluca.dscommerce.services;

import com.rafaeldeluca.dscommerce.dto.OrderDTO;
import com.rafaeldeluca.dscommerce.entities.Order;
import com.rafaeldeluca.dscommerce.entities.OrderItem;
import com.rafaeldeluca.dscommerce.entities.Product;
import com.rafaeldeluca.dscommerce.entities.User;
import com.rafaeldeluca.dscommerce.repositories.OrderItemRepository;
import com.rafaeldeluca.dscommerce.repositories.OrderRepository;
import com.rafaeldeluca.dscommerce.repositories.ProductRepository;
import com.rafaeldeluca.dscommerce.services.exceptions.ForbiddenException;
import com.rafaeldeluca.dscommerce.services.exceptions.ResourceNotFoundException;
import com.rafaeldeluca.dscommerce.tests.OrderFactory;
import com.rafaeldeluca.dscommerce.tests.ProductFactory;
import com.rafaeldeluca.dscommerce.tests.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AuthService authService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserService userService;

    // com injectMock tem que usar a funcao Mockito.spy() para simular os dados mockados
    @InjectMocks
    private OrderService orderService;

    private String userNameClient, userNameAdmin;
    private User userClient, userAdmin;
    private Long existingOrderId, nonExistingOrderId;
    private Long existingProductId, nonExistingProductId;
    private Order order, orderAdmin;
    private OrderDTO orderDTO, orderDTOAdmin;
    private Product product, product2;

    @BeforeEach
    void setUp () throws Exception {

        userNameClient = "juliana@gmail.com";
        userNameAdmin = "deluca1712@gmail.com";

        existingOrderId = 1L;
        nonExistingOrderId = 50L;

        existingProductId = 1l;
        nonExistingProductId = 50L;

        userClient = UserFactory.createCustomUserClient(1L, userNameClient);
        userAdmin = UserFactory.createCustomAdminUser(2l, userNameAdmin);

        order = OrderFactory.createOrder(userClient);

        orderAdmin = OrderFactory.createOrder(userAdmin);

        orderDTO = new OrderDTO(order);
        orderDTOAdmin = new OrderDTO(orderAdmin);

        product = ProductFactory.createProduct();
        product2 = ProductFactory.createProduct();

        // mocando buscar pedido por Id
        Mockito.when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        // mocando a busca de um produto por id
        Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

        // mocando salvar um pedido
        Mockito.when(orderRepository.save(any())).thenReturn(order);


        // mocando salvar (adicionar) os itens do pedido no pedido
        Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));

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

    @Test
    public void findByIdShouldReturnThrowsResourceNotFoundExceptionWhenIdOrderDoNotExists () {
        Mockito.doThrow(ResourceNotFoundException.class).when(authService).validateSelfOrAdmin(any());

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> {
           OrderDTO orderDTO = orderService.findById(nonExistingOrderId);
        });
    }

    @Test
    public void insertShouldReturnOrderDTOWhenUserLoggedAsAdmin () {
        // mockar usuário autenticado como admin
        Mockito.when(userService.authenticated()).thenReturn(userAdmin);

        OrderDTO result = orderService.insert(orderDTOAdmin);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getItems().size(),1);

    }

    @Test
    public void insertShouldReturnOrderDTOWhenUserLoggedAsClient() {
        // mockar usuário autenticado como client
        Mockito.when(userService.authenticated()).thenReturn(userClient);

        OrderDTO result = orderService.insert(orderDTO);
        Assertions.assertEquals(result.getItems().size(),1);

        Assertions.assertNotNull(result);
    }

    @Test
    public void insertShouldThrowsUserNameNotFountExceptionWhenUserNotLogged () {
        // sem mockar usuário autenticado tem que retonar uma exeção
        Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();

        // usuário vazio
        order.setClient(new User());
        orderDTO = new OrderDTO(order);

        Assertions.assertThrows(UsernameNotFoundException.class, ()-> {
            OrderDTO orderServiceResult = orderService.insert(orderDTO);
        });
    }

    @Test
    public void insertShouldThrowsEntityNotFoundExceptionWhenOrderProductIdDoesNotExist () {

        Mockito.when(userService.authenticated()).thenReturn(userClient);

        // simulando um produto com id inexistente
        product.setId(nonExistingProductId);
        OrderItem orderItem = new OrderItem(order, product, 10, 2500.50);
        order.getItems().add(orderItem);

        orderDTO = new OrderDTO(order);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            OrderDTO orderServiceResult = orderService.insert(orderDTO);
        });
    }
}