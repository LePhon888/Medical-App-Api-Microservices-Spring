package com.med.service;

import com.med.model.Medicine;
import com.med.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MedicineService {
    @Autowired
    private MedicineRepository repository;

    public Page<Medicine> getMedicines(Map<String, String> params) {
        int pageSize = 20;
        int pageNumber = Integer.parseInt(params.getOrDefault("page", "0"));
        String name = params.get("name");
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        if (name != null && !name.isEmpty()) {
            return repository.findByNameContainingIgnoreCase(name, pageRequest);
        } else {
            // If name is empty, retrieve all medicines without filtering by name
            return repository.findAll(pageRequest);
        }
    }
}
