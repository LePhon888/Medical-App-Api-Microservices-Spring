package com.med.service;

import com.med.dto.DoctorDTO;
import com.med.dto.DoctorDetailsDTO;
import com.med.model.DoctorDetails;
import com.med.repository.DoctorDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorDetailsService {
    @Autowired
    private DoctorDetailsRepository repository;

    public List<DoctorDetailsDTO> getDoctorDetailsByDoctorId(String doctorId) {
        List<Object[]> result = this.repository.getDoctorDetailsByDoctorId(doctorId);
        return result.stream()
                .map(DoctorDetailsDTO::new)
                .collect(Collectors.toList());
    }
}
