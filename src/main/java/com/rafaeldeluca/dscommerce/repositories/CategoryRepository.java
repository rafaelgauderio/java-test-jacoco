package com.rafaeldeluca.dscommerce.repositories;

import com.rafaeldeluca.dscommerce.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
