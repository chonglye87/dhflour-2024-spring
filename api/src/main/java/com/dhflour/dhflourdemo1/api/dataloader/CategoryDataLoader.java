package com.dhflour.dhflourdemo1.api.dataloader;

import com.dhflour.dhflourdemo1.core.domain.category.CategoryEntity;
import com.dhflour.dhflourdemo1.core.domain.category.CategoryEntityRepository;
import com.dhflour.dhflourdemo1.core.service.category.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1)
public class CategoryDataLoader implements CommandLineRunner {

    @Autowired
    private CategoryEntityRepository categoryEntityRepository;

    @Autowired
    private CategoryService categoryService;

    @Override
    public void run(String... args) throws Exception {
        log.debug("::: CategoryDataLoader:Run :::");

        if (categoryEntityRepository.findAll().isEmpty()) {
            this.create("공지");
            this.create("점검");
        }
    }

    private CategoryEntity create(String name) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(name);
        return categoryService.create(categoryEntity);
    }
}
