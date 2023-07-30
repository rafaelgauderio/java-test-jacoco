package com.rafaeldeluca.dscommerce.tests;

import com.rafaeldeluca.dscommerce.entities.Category;

public class CategoryFactory {

    public static Category createCategory () {
        return new Category(1L, "Eletronics");

    }

    public static Category createCategory (Long id, String name) {
        return new Category(id, name);
    }
}
