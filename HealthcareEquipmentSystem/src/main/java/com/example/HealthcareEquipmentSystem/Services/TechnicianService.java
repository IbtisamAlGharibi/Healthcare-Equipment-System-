package com.example.HealthcareEquipmentSystem.Services;

import com.example.HealthcareEquipmentSystem.DTO.Requests.TechnicianRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.TechnicianResponseDTO;
import com.example.HealthcareEquipmentSystem.Entities.MaintenanceTechnician;
import com.example.HealthcareEquipmentSystem.Repositories.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TechnicianService {
    private TechnicianRepository technicianRepository;

    @Autowired
    public TechnicianService(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    // Add Technician
    public TechnicianResponseDTO addTechnician(TechnicianRequestDTO dto) {

        MaintenanceTechnician technician = TechnicianRequestDTO.toEntity(dto);
        MaintenanceTechnician savedTechnician = technicianRepository.save(technician);
        return TechnicianResponseDTO.fromEntity(savedTechnician);
    }

    // Update Technician
    public TechnicianResponseDTO updateTechnician(Integer id, TechnicianRequestDTO dto) {

        MaintenanceTechnician technician = technicianRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Technician not found with id: " + id));
        technician.setName(dto.getName());
        technician.setPhone(dto.getPhone());
        technician.setSpecialization(dto.getSpecialization());
        technician.setIsActive(dto.getIsActive());
        MaintenanceTechnician updatedTechnician = technicianRepository.save(technician);
        return TechnicianResponseDTO.fromEntity(updatedTechnician);
    }

    // Delete Technician
    public void deleteTechnician(Integer id) {
        if (!technicianRepository.existsById(id)) {
            throw new EntityNotFoundException("Technician not found with id: " + id);
        }
        technicianRepository.deleteById(id);
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

        List<MaintenanceTechnician> technicians = technicianRepository.findBySpecializationAndIsActive(specialization);
        List<TechnicianResponseDTO> response = new ArrayList<>();
        for (MaintenanceTechnician technician : technicians) {
            response.add(TechnicianResponseDTO.fromEntity(technician));
        }
        return response;
    }

    //Get Technician By ID
    public TechnicianResponseDTO getTechnicianById(Integer id) {
        MaintenanceTechnician technician = technicianRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Technician not found with id: " + id));
        return TechnicianResponseDTO.fromEntity(technician);
    }
}
