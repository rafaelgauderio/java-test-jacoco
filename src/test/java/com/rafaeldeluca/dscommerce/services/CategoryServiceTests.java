package com.rafaeldeluca.dscommerce.services;

import com.rafaeldeluca.dscommerce.dto.CategoryDTO;
import com.rafaeldeluca.dscommerce.entities.Category;
import com.rafaeldeluca.dscommerce.repositories.CategoryRepository;
import com.rafaeldeluca.dscommerce.tests.CategoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ExtendWith(SpringExtension.class) // não carregar o contexto da aplicação
public class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks // injeção de dependencia
    private CategoryService categoryService;

    private Category category;
    private Category categoryBooks;
    private List<Category> categoryList;

    @BeforeEach // executar o setUp antes de cada teste
    void setUp () throws Exception {

        // boa prática - instanciar a entidade a partir de um factoru
        category = CategoryFactory.createCategory();
        categoryBooks = CategoryFactory.createCategory(2L,"Books");

        categoryList = new ArrayList<Category>();
        categoryList.add(category);
        categoryList.add(categoryBooks);

        // mockito para simular o comportamento
        Mockito.when(categoryRepository.findAll()).thenReturn(categoryList);
    }

    @Test
    public void findAllShoudlReturnListCatedoryDTO () {

        List<CategoryDTO>  result = categoryService.findAll();

        Assertions.assertEquals(result.size(),2);
        Assertions.assertEquals(result.get(0).getId(), 1L);
        Assertions.assertEquals(result.get(0).getName(), "Eletronics");
        Assertions.assertEquals(result.get(1).getId(),2L);
        Assertions.assertEquals(result.get(1).getName(), "Books");
    }
}
