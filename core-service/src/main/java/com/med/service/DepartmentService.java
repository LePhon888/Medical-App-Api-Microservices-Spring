package com.med.service;

import com.med.model.Department;
import com.med.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public Department getById (int id) {
        return departmentRepository.findById(id).orElse(null);
    }
    public List<Department> getAll () {
        return departmentRepository.findAll();
    }

}
