package com.med.service;


import com.med.dto.RatingDTO;
import com.med.model.Rating;
import com.med.repository.DoctorRepository;
import com.med.repository.RatingRepository;
import com.med.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public List<RatingDTO> getRatingByDoctorId(String id) {
        return this.ratingRepository.getRatingByDoctorId(id);
    }

    public List<Object[]> getRatingStatsByDoctorId(String id) {
        return this.ratingRepository.getRatingStatsByDoctorId(id);
    }

    public String createOrUpdateRating(Map<String, String> Payload) {
        try {
            Rating rating = new Rating();
            rating.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            rating.setStar(Integer.valueOf(Payload.get("star")));
            rating.setComment(Payload.get("comment"));
            rating.setUser(userRepository.findById(Integer.valueOf(Payload.get("userId"))).get());
            rating.setDoctor(doctorRepository.findById(Integer.valueOf(Payload.get("doctorId"))).get());
            ratingRepository.save(rating);
            return "";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}
