package com.med.service;

import com.med.model.Appointment;
import com.med.model.User;
import com.med.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    public Appointment create(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }
    public Appointment getById(int id) {
        return appointmentRepository.findById(id).orElse(null);
    }
    public List<Appointment> getAppointmentsByRegisterUser(User u) {
        return appointmentRepository.findByRegisterUser(u);
    }
}
