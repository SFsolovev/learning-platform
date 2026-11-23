package com.example.learning_platform.web;

import com.example.learning_platform.dto.CategoryResponse;
import com.example.learning_platform.dto.CreateCategoryRequest;
import com.example.learning_platform.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@Valid @RequestBody CreateCategoryRequest r) {
        return CategoryResponse.from(categoryService.create(r.name()));
    }

    @GetMapping
    public List<CategoryResponse> all() {
        return categoryService.getAll().stream().map(CategoryResponse::from).toList();
    }
}