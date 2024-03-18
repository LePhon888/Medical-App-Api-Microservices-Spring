package com.med.service;

import com.med.model.Category;
import com.med.model.Department;
import com.med.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll () {
        return categoryRepository.findAll();
    }

}