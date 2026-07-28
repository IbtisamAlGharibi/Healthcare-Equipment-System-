package com.example.HealthcareEquipmentSystem.Services;

import com.example.HealthcareEquipmentSystem.Repositories.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TechnicianService {
    private TechnicianRepository technicianRepository;
    @Autowired
    public TechnicianService(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

}
