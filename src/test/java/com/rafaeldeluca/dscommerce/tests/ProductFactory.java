package com.rafaeldeluca.dscommerce.tests;

import com.rafaeldeluca.dscommerce.entities.Category;
import com.rafaeldeluca.dscommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct () {
        Category category = CategoryFactory.createCategory();
        Product product = new Product(2L,"Tablet Sansung", "Table Sansung com 16 giga de memória", 4900.90,"https://www.sansung/tableImage.png");
        product.getCategories().add(category);
        return product;
    }

    public static Product createProduct (String name) {
        Product product = ProductFactory.createProduct();
        product.setName(name);
        return product;
    }
}
