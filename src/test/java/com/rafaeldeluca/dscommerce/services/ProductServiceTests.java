package com.rafaeldeluca.dscommerce.services;

import com.rafaeldeluca.dscommerce.dto.ProductDTO;
import com.rafaeldeluca.dscommerce.entities.Product;
import com.rafaeldeluca.dscommerce.repositories.ProductRepository;
import com.rafaeldeluca.dscommerce.services.exceptions.ResourceNotFoundException;
import com.rafaeldeluca.dscommerce.tests.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Long existingProductId, nonExistingProductId;
    private Product product;

    private Product product2;
    private String productName;

    @BeforeEach
    void setUp () throws Exception {

        existingProductId = 2L;
        nonExistingProductId = 50L;

        productName = "Tablet Sansung";

        product = ProductFactory.createProduct(productName);

        product2 = ProductFactory.createProduct();

        Mockito.when(productRepository.findById(existingProductId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(existingProductId)).thenReturn(Optional.of(product2));

        // to non existing Id
        //Mockito.when(productRepository.findById(nonExistingProductId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(productRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());

    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists () {

        ProductDTO result = productService.findById(existingProductId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingProductId);
        Assertions.assertEquals(result.getName(), product.getName());

        Assertions.assertEquals(result.getPrice(), product2.getPrice());
        Assertions.assertEquals(result.getImgUrl(), product2.getImgUrl());
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist () {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {

            productService.findById(nonExistingProductId);

        });

    }


}
