package com.rafaeldeluca.dscommerce.services;

import com.rafaeldeluca.dscommerce.dto.ProductDTO;
import com.rafaeldeluca.dscommerce.dto.ProductMinDTO;
import com.rafaeldeluca.dscommerce.entities.Product;
import com.rafaeldeluca.dscommerce.repositories.ProductRepository;
import com.rafaeldeluca.dscommerce.services.exceptions.DatabaseException;
import com.rafaeldeluca.dscommerce.services.exceptions.ResourceNotFoundException;
import com.rafaeldeluca.dscommerce.tests.ProductFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    private Long existingProductId, nonExistingProductId, dependentProductId;
    private Product product;
    private Product product2;
    private String productName;
    private ProductDTO productDTO;

    private PageImpl<Product> pageImpl;

    @BeforeEach
    void setUp () throws Exception {

        existingProductId = 1L;
        nonExistingProductId = 50L;
        dependentProductId =2L;

        productName = "Tablet Sansung";

        product = ProductFactory.createProduct(productName);
        product2 = ProductFactory.createProduct();

        productDTO = new ProductDTO(product);

        pageImpl = new PageImpl<Product>(List.of(product));

        // mock data to the method findById or searchByname
        Mockito.when(productRepository.findById(existingProductId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(existingProductId)).thenReturn(Optional.of(product2));
        //Mockito.when(productRepository.findById(nonExistingProductId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(productRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());
        Mockito.when(productRepository.searchByName(any(),(Pageable) any())).thenReturn(pageImpl);

        // mock data to insert
        Mockito.when(productRepository.save(any())).thenReturn(product);

        // mock data to update product with existingId and NotFoundId
        Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

        //mock data to delete method
        Mockito.when(productRepository.existsById(existingProductId)).thenReturn(true);
        Mockito.when(productRepository.existsById(dependentProductId)).thenReturn(true);
        Mockito.when(productRepository.existsById(nonExistingProductId)).thenReturn(false);

        Mockito.doNothing().when(productRepository).deleteById(existingProductId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentProductId);

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

    @Test
    public void findByIdShouldReturnPagedProductMinDTO () {
        Pageable pageable = PageRequest.of(0,24);

        Page<ProductMinDTO> serviceResult = productService.findAll(productName, pageable);

        Assertions.assertNotNull(serviceResult);
        Assertions.assertEquals(serviceResult.getSize(),1);
        Assertions.assertEquals(serviceResult.iterator().next().getName(), productName);
    }

    @Test
    public void findByIdShouldReturnPagedProductMinDTOWithNullPageable () {

        Page<ProductMinDTO> serviceResult = productService.findAll(productName, null);

        Assertions.assertNotNull(serviceResult);
        Assertions.assertEquals(serviceResult.getSize(),1);
        Assertions.assertEquals(serviceResult.iterator().next().getName(), productName);
    }

    @Test
    public void insertProductShouldReturnProductDTO () {

        ProductDTO serviceResult = productService.insert(productDTO);

        Assertions.assertNotNull(serviceResult);
        Assertions.assertEquals(serviceResult.getId(), product.getId());
        Assertions.assertEquals(serviceResult.getName(), product.getName());
    }

    @Test
    public void updateProductShouldReturnProductDTOWithExistingId () {

        ProductDTO serviceResult = productService.update(existingProductId,productDTO);

        Assertions.assertNotNull(serviceResult);
        Assertions.assertEquals(serviceResult.getId(), existingProductId);
        Assertions.assertEquals(serviceResult.getName(), productDTO.getName());
    }

    @Test
    public void updateProductShouldReturnProductDTOWWithNonExistingId () {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.update(nonExistingProductId, productDTO);
        });

    }

    @Test
    public void deleteShouldDoNotThrowExceptionWhenProductIdExists () {
        Assertions.assertDoesNotThrow( () -> {
            productService.delete(existingProductId);
        });
    }

    @Test
    public void deleteShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           productService.delete(nonExistingProductId);
        });
    }

    @Test
    public void deleteShouldReturnDataBaseExceptionWhenProductIdIsDepend () {
        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(dependentProductId);
        });
    }
}
