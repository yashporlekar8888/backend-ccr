package com.ccr.mvp;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.ccr.mvp.model.Category;
import com.ccr.mvp.repository.CategoryRepository;
import com.ccr.mvp.service.CategoryService;
import com.ccr.mvp.service.impl.CategoryServiceImpl;


@SpringBootTest
class CcrMvpApplicationTests {
	
	   @Mock
	    private CategoryRepository categoryRepository;

	    @InjectMocks
	    private CategoryServiceImpl categoryService;
	
	@Test
	void contextLoads() {
	}
	@Test
    void testCreateCategory() {
        Category category = new Category();
        category.setCategoryName("Test Category");
        
        // Mock the behavior of categoryRepository.save method
        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);

        Category createdCategory = categoryService.createCategory(category);

        // Verify that the categoryRepository.save method was called with the correct argument
        Mockito.verify(categoryRepository, Mockito.times(1)).save(Mockito.any(Category.class));

        // Add more assertions based on the expected behavior of the createCategory method
        assert createdCategory != null;
        assert createdCategory.getCategoryName().equals("Test Category");
        assert createdCategory.getCategoryWeightage().equals(0.0);
        // Add more assertions based on the expected behavior of the method

        // Example: Verify that the createdAt field is set properly
        // assert createdCategory.getCreatedAt() != null;
    }

}
