package com.medical.medicationservice.service;
import com.medical.medicationservice.model.Medicine;
import com.medical.medicationservice.repository.MedicineRepository;
import com.medical.medicationservice.repository.MedicineUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MedicineService {
    @Autowired
    private MedicineRepository repository;
    @Autowired
    private MedicineUnitRepository medicineUnitRepository;
    @Autowired
    private MedicineRepository medicineRepository;
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
    public List<Medicine> getAllMedicines() {
        return this.medicineRepository.findAll();
    }

}
