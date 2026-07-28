package com.example.HealthcareEquipmentSystem.Services;

import com.example.HealthcareEquipmentSystem.DTO.Requests.MaintenanceRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.MaintenanceResponseDTO;
import com.example.HealthcareEquipmentSystem.Entities.Equipment;
import com.example.HealthcareEquipmentSystem.Entities.Maintenance;
import com.example.HealthcareEquipmentSystem.Entities.MaintenanceTechnician;
import com.example.HealthcareEquipmentSystem.Repositories.EquipmentRepository;
import com.example.HealthcareEquipmentSystem.Repositories.MaintenanceRepository;
import com.example.HealthcareEquipmentSystem.Repositories.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceService {
    private TechnicianRepository technicianRepository;
    private MaintenanceRepository maintenanceRepository;
    private EquipmentRepository equipmentRepository;

    @Autowired
    public MaintenanceService(MaintenanceRepository maintenanceRepository, TechnicianRepository technicianRepository, EquipmentRepository equipmentRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.technicianRepository = technicianRepository;
        this.equipmentRepository = equipmentRepository;
    }

    //  Create Maintenance
    public MaintenanceResponseDTO createMaintenance(MaintenanceRequestDTO dto) {

        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId()).get();
        Maintenance maintenance = MaintenanceRequestDTO.toEntity(dto);
        MaintenanceTechnician technician=technicianRepository.findById(dto.getTechnicianId()).get();
        maintenance.setEquipment(equipment);
        maintenance.setTechnician(technician);
        equipment.setStatus("UNDER_MAINTENANCE");
        technician.setIsActive(true);
        equipmentRepository.save(equipment);
        technicianRepository.save(technician);
        Maintenance saved = maintenanceRepository.save(maintenance);
        return MaintenanceResponseDTO.fromEntity(saved);
    }

    //  Complete Maintenance
    public MaintenanceResponseDTO completeMaintenance(Integer id) {

        Maintenance maintenance = maintenanceRepository.findById(id).get();
        maintenance.setStatus("COMPLETE");
        Equipment equipment = maintenance.getEquipment();;
        equipment.setStatus("AVAILABLE");
        equipmentRepository.save(equipment);
        Maintenance updated = maintenanceRepository.save(maintenance);
        return MaintenanceResponseDTO.fromEntity(updated);
    }

    //  Get All Maintenance
    public List<MaintenanceResponseDTO> getAllMaintenance() {
        return maintenanceRepository.findAll().stream().map(MaintenanceResponseDTO::fromEntity).toList();
    }

    //  Get Maintenance By ID
    public MaintenanceResponseDTO getMaintenanceById(Integer id) {
        Maintenance maintenance = maintenanceRepository.findById(id).get();
        return MaintenanceResponseDTO.fromEntity(maintenance);
    }

    // Get Maintenance By Status
    public List<MaintenanceResponseDTO> getMaintenanceByStatus(String status) {
        return maintenanceRepository.findByStatus(status).stream().map(MaintenanceResponseDTO::fromEntity).toList();
    }

    // Get Maintenance By Technician ID
    public List<MaintenanceResponseDTO> getMaintenanceByTechnicianId(Integer technicianId) {

        return maintenanceRepository.findByTechnicianId(technicianId).stream().map(MaintenanceResponseDTO::fromEntity).toList();
    }

    // Get Maintenance By Equipment ID
    public List<MaintenanceResponseDTO> getMaintenanceByEquipmentId(Integer equipmentId) {

        return maintenanceRepository.findByEquipmentId(equipmentId).stream().map(MaintenanceResponseDTO::fromEntity).toList();
    }

    // Get Maintenance Between Two Dates
    public List<MaintenanceResponseDTO> getMaintenanceBetweenDates(LocalDate startDate, LocalDate endDate) {
        return maintenanceRepository.findMaintenanceBetweenDates(startDate, endDate).stream().map(MaintenanceResponseDTO::fromEntity).toList();
    }

    //  Delete Maintenance
    public String deleteMaintenance(Integer id) {
        if (!maintenanceRepository.existsById(id)) {
            //throw new EntityNotFoundException("Maintenance record not found with id: " + id);
        }
        Maintenance maintenance=maintenanceRepository.findById(id).get();
        maintenance.setStatus("PENDING");
        maintenanceRepository.save(maintenance);
        return "Maintenance Has Been Deleted...";
    }
}
