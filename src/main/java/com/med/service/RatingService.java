package com.med.service;


import com.med.dto.RatingDTO;
import com.med.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingService {
    @Autowired
    private RatingRepository repository;

    public List<RatingDTO> getRatingByDoctorId(String id) {
        List<Object[]> result = this.repository.getRatingByDoctorId(id);
        return result.stream().map(RatingDTO::new).collect(Collectors.toList());
    }

    public List<Object[]> getRatingStatsByDoctorId(String id) {
        return this.repository.getRatingStatsByDoctorId(id);
    }
}
