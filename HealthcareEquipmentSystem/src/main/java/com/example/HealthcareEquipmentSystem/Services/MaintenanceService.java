package com.example.HealthcareEquipmentSystem.Services;

import com.example.HealthcareEquipmentSystem.DTO.Requests.MaintenanceRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.MaintenanceResponseDTO;
import com.example.HealthcareEquipmentSystem.Entities.Equipment;
import com.example.HealthcareEquipmentSystem.Entities.Maintenance;
import com.example.HealthcareEquipmentSystem.Entities.MaintenanceTechnician;
import com.example.HealthcareEquipmentSystem.Entities.Reservation;
import com.example.HealthcareEquipmentSystem.Exceptions.BadRequestException;
import com.example.HealthcareEquipmentSystem.Exceptions.ResourceNotFoundException;
import com.example.HealthcareEquipmentSystem.Repositories.EquipmentRepository;
import com.example.HealthcareEquipmentSystem.Repositories.MaintenanceRepository;
import com.example.HealthcareEquipmentSystem.Repositories.ReservationRepository;
import com.example.HealthcareEquipmentSystem.Repositories.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

        Equipment equipment = equipmentRepository.findByEquipmentId(dto.getEquipmentId());
        if (equipment == null) {
            throw new ResourceNotFoundException("Equipment not found with id: " + dto.getEquipmentId());
        }

        MaintenanceTechnician technician = technicianRepository.findByMaintenanceTechnicianId(dto.getTechnicianId());
        if (technician == null) {
            throw new ResourceNotFoundException("Technician not found with id: " + dto.getTechnicianId());
        }
        if (equipment.getStatus().equalsIgnoreCase("Reserved")) {
            throw new ResourceNotFoundException("Equipment with id:" + dto.getEquipmentId()+" is RESERVED");
        }

        if (Boolean.FALSE.equals(technician.getIsActive())) {
            throw new BadRequestException("This technician member is not active.");
        }
        if (Boolean.FALSE.equals(equipment.getIsActive())) {
            throw new BadRequestException("This equipment has been deleted.");
        }
        Maintenance maintenance = MaintenanceRequestDTO.toEntity(dto);
        maintenance.setEquipment(equipment);
        maintenance.setTechnician(technician);
        maintenance.setStatus("UNDER-MAINTENANCE");
        equipment.setStatus("MAINTENANCE");
        technician.setIsActive(true);

        equipmentRepository.save(equipment);
        technicianRepository.save(technician);
        Maintenance saved = maintenanceRepository.save(maintenance);
        return MaintenanceResponseDTO.fromEntity(saved);
    }

    //  Complete Maintenance
    public MaintenanceResponseDTO completeMaintenance(Integer id) {

        Maintenance maintenance = maintenanceRepository.findByMaintenanceId(id);
        if (maintenance == null) {
            throw new ResourceNotFoundException("Maintenance record not found with id: " + id);
        }

        maintenance.setStatus("Completed");
        Equipment equipment = maintenance.getEquipment();
        if (equipment != null) {
            equipment.setStatus("Available");
            equipmentRepository.save(equipment);
        }

        Maintenance updated = maintenanceRepository.save(maintenance);
        return MaintenanceResponseDTO.fromEntity(updated);
    }

    //  Get All Maintenance
    public List<MaintenanceResponseDTO> getAllMaintenance() {
        List<Maintenance> maintenanceList = maintenanceRepository.getAllMaintenance();
        List<MaintenanceResponseDTO> responseDTOList = new ArrayList<>();
        for (Maintenance maintenance : maintenanceList) {
            responseDTOList.add(MaintenanceResponseDTO.fromEntity(maintenance));
        }
        return responseDTOList;
    }

    //  Get Maintenance By ID
    public MaintenanceResponseDTO getMaintenanceById(Integer id) {
        Maintenance maintenance = maintenanceRepository.findByMaintenanceId(id);
        if (maintenance == null) {
            throw new ResourceNotFoundException("Maintenance record not found with id: " + id);
        }
        return MaintenanceResponseDTO.fromEntity(maintenance);
    }

    // Get Maintenance By Status
    public List<MaintenanceResponseDTO> getMaintenanceByStatus(String status) {
        List<Maintenance> maintenanceList = maintenanceRepository.findByStatus(status);
        List<MaintenanceResponseDTO> responseDTOList = new ArrayList<>();
        for (Maintenance maintenance : maintenanceList) {
            responseDTOList.add(MaintenanceResponseDTO.fromEntity(maintenance));
        }
        return responseDTOList;
    }
    // Get Maintenance By Technician ID
    public List<MaintenanceResponseDTO> getMaintenanceByTechnicianId(Integer technicianId) {
        List<Maintenance> maintenanceList = maintenanceRepository.findByTechnicianId(technicianId);
        List<MaintenanceResponseDTO> responseDTOList = new ArrayList<>();
        for (Maintenance maintenance : maintenanceList) {
            responseDTOList.add(MaintenanceResponseDTO.fromEntity(maintenance));
        }
        return responseDTOList;
    }

    // Get Maintenance By Equipment ID
    public List<MaintenanceResponseDTO> getMaintenanceByEquipmentId(Integer equipmentId) {
        List<Maintenance> maintenanceList = maintenanceRepository.findByEquipmentId(equipmentId);
        List<MaintenanceResponseDTO> responseDTOList = new ArrayList<>();
        for (Maintenance maintenance : maintenanceList) {
            responseDTOList.add(MaintenanceResponseDTO.fromEntity(maintenance));
        }
        return responseDTOList;
    }

    // Get Maintenance Between Two Dates
    public List<MaintenanceResponseDTO> getMaintenanceBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<Maintenance> maintenanceList = maintenanceRepository.findMaintenanceBetweenDates(startDate, endDate);
        List<MaintenanceResponseDTO> responseDTOList = new ArrayList<>();
        for (Maintenance maintenance : maintenanceList) {
            responseDTOList.add(MaintenanceResponseDTO.fromEntity(maintenance));
        }
        return responseDTOList;
    }

    //  Delete Maintenance
    public void deleteMaintenance(Integer id) {
        Maintenance maintenance = maintenanceRepository.findByMaintenanceId(id);
        if (maintenance == null) {
            throw new ResourceNotFoundException("Maintenance record not found with id: " + id);
        }
        maintenance.setIsActive(false);
        maintenance.setStatus("CANCELLED");
        maintenanceRepository.save(maintenance);
    }
}
