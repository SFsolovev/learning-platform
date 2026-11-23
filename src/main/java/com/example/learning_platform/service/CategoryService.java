package com.example.learning_platform.service;

import com.example.learning_platform.entity.Category;
import com.example.learning_platform.exception.BusinessException;
import com.example.learning_platform.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category create(String name) {
        if (categoryRepository.findByName(name).isPresent()) {
            throw new BusinessException("Категория уже существует: " + name);
        }
        Category c = new Category();
        c.setName(name);
        return categoryRepository.save(c);
    }

    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}