package com.med.service;

import com.med.model.Appointment;
import com.med.model.Medicine;
import com.med.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    public Medicine createPost(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    public Medicine updatePost(int id, Medicine medicineRequest) {
        Medicine medicine = medicineRepository.findById(id)
                .orElse(null);

        medicine.setName(medicineRequest.getName());
        return medicineRepository.save(medicine);
    }

    public void deletePost(int id) {
        Medicine medicine = medicineRepository.findById(id).orElse(null);
        assert medicine != null;
        medicineRepository.delete(medicine);
    }

    public Medicine getById(int id) {
        return medicineRepository.findById(id).orElse(null);
    }
}
