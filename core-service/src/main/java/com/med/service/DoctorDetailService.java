package com.med.service;

import com.med.dto.DoctorDetailDTO;
import com.med.repository.DoctorDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorDetailService {
    @Autowired
    private DoctorDetailRepository repository;

    public List<DoctorDetailDTO> getDoctorDetailsByDoctorId(String doctorId) {
        return this.repository.getDoctorDetailsByDoctorId(doctorId);
    }
}
