package com.example.HealthcareEquipmentSystem.Services;

import com.example.HealthcareEquipmentSystem.DTO.Requests.TechnicianRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.TechnicianResponseDTO;
import com.example.HealthcareEquipmentSystem.Entities.MaintenanceTechnician;
import com.example.HealthcareEquipmentSystem.Exceptions.BadRequestException;
import com.example.HealthcareEquipmentSystem.Exceptions.ResourceNotFoundException;
import com.example.HealthcareEquipmentSystem.Repositories.TechnicianRepository;
import com.example.HealthcareEquipmentSystem.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TechnicianService {
    private TechnicianRepository technicianRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public TechnicianService(TechnicianRepository technicianRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.technicianRepository = technicianRepository;
        this.userRepository= userRepository;
        this.passwordEncoder=passwordEncoder;
    }
    // Add Technician
    public TechnicianResponseDTO addTechnician(TechnicianRequestDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new BadRequestException("Username is already taken");
        }
        MaintenanceTechnician technician = TechnicianRequestDTO.toEntity(dto);
        technician.setIsActive(true);
        technician.getUser().setPassword(passwordEncoder.encode(technician.getUser().getPassword()));

        MaintenanceTechnician savedTechnician = technicianRepository.save(technician);
        return TechnicianResponseDTO.fromEntity(savedTechnician);
    }

    // Update Technician
    public TechnicianResponseDTO updateTechnician(Integer id, TechnicianRequestDTO dto) {
        if(!technicianRepository.existsById(id)){
            throw new ResourceNotFoundException("Technician not found with id: " + id);
        }
        MaintenanceTechnician technician = technicianRepository.findByMaintenanceTechnicianId(id);
        technician.setName(dto.getName());
        technician.setPhone(dto.getPhone());
        technician.setSpecialization(dto.getSpecialization());
        //technician.setIsActive(dto.getIsActive());
        MaintenanceTechnician updatedTechnician = technicianRepository.save(technician);
        return TechnicianResponseDTO.fromEntity(updatedTechnician);
    }

    // Delete Technician
    public void deleteTechnician(Integer id) {
        if(!technicianRepository.existsById(id)){
            throw new ResourceNotFoundException("Technician not found with id: " + id);
        }
        MaintenanceTechnician technician = technicianRepository.findByMaintenanceTechnicianId(id);
        technician.setIsActive(false);
        technicianRepository.save(technician);
    }

    // Get All Technicians
    public List<TechnicianResponseDTO> getAllTechnicians() {
        List<MaintenanceTechnician> technicians = technicianRepository.findAll();
        List<TechnicianResponseDTO> response = new ArrayList<>();
        for (MaintenanceTechnician technician : technicians) {
            response.add(TechnicianResponseDTO.fromEntity(technician));
        }
        return response;
    }

    // Get All Active Technicians
    public List<TechnicianResponseDTO> getAllActiveTechnicians() {
        List<MaintenanceTechnician> technicians = technicianRepository.findAllByIsActive();
        List<TechnicianResponseDTO> response = new ArrayList<>();
        for (MaintenanceTechnician technician : technicians) {
            response.add(TechnicianResponseDTO.fromEntity(technician));
        }
        return response;
    }

    // Get Technicians By Specialization
    public List<TechnicianResponseDTO> getTechniciansBySpecialization(String specialization) {
        List<MaintenanceTechnician> technicians = technicianRepository.findBySpecialization(specialization);
        List<TechnicianResponseDTO> response = new ArrayList<>();
        for (MaintenanceTechnician technician : technicians) {
            response.add(TechnicianResponseDTO.fromEntity(technician));
        }
        return response;
    }

    // Get Active Technicians By Specialization
    public List<TechnicianResponseDTO> getActiveTechniciansBySpecialization(String specialization) {
        if(technicianRepository.findBySpecialization(specialization).isEmpty()) {
            throw new ResourceNotFoundException("Technician not found with specialization: " + specialization);
        }
        List<MaintenanceTechnician> technicians = technicianRepository.findBySpecializationAndIsActive(specialization);
        List<TechnicianResponseDTO> response = new ArrayList<>();
        for (MaintenanceTechnician technician : technicians) {
            response.add(TechnicianResponseDTO.fromEntity(technician));
        }
        return response;
    }

    //Get Technician By ID
    public TechnicianResponseDTO getTechnicianById(Integer id) {
        MaintenanceTechnician technician = technicianRepository.findByMaintenanceTechnicianId(id);
        return TechnicianResponseDTO.fromEntity(technician);
    }
    public TechnicianResponseDTO getMyProfile(String username) {
        MaintenanceTechnician technician = technicianRepository.findByUserUsername(username);
        if (technician == null) {
            throw new ResourceNotFoundException("No technician profile linked to this account.");
        }
        return TechnicianResponseDTO.fromEntity(technician);
    }
}
