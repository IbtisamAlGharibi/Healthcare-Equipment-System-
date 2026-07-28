package com.example.HealthcareEquipmentSystem.Controllers;

import com.example.HealthcareEquipmentSystem.DTO.Requests.MaintenanceRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.MaintenanceResponseDTO;
import com.example.HealthcareEquipmentSystem.Entities.Maintenance;
import com.example.HealthcareEquipmentSystem.Repositories.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    private EquipmentRepository equipmentRepository;
    private MaintenanceRepository maintenanceRepository;
    private LaboratoryRepository laboratoryRepository;

    @Autowired
    public ReportController(EquipmentRepository equipmentRepository, MaintenanceRepository maintenanceRepository, LaboratoryRepository laboratoryRepository){
        this.equipmentRepository=equipmentRepository;
        this.maintenanceRepository=maintenanceRepository;
    this.laboratoryRepository=laboratoryRepository;
    }

    @GetMapping("/equipment/{status}")
    public ResponseEntity<List<EquipmentResponseDTO>> getEquipmentReport(@PathVariable String status) {
        List<Equipment> filteredEquipment = new ArrayList<>();

        if ("Available".equalsIgnoreCase(status)) {
            filteredEquipment = equipmentRepository.findByStatus("Available");
        } else if ("Reserved".equalsIgnoreCase(status)) {
            filteredEquipment = equipmentRepository.findByStatus("Reserved");
        } else if ("Maintenance".equalsIgnoreCase(status)) {
            filteredEquipment = equipmentRepository.findByStatus("Maintenance");
        } else {
            return ResponseEntity.badRequest().build();
        }

        List<EquipmentResponseDTO> responseDTOs = new ArrayList<>();
        for (Equipment eq : filteredEquipment) {
            EquipmentResponseDTO dto = new EquipmentResponseDTO();
            dto.setId(eq.getId());
            dto.setName(eq.getName());
            dto.setStatus(eq.getStatus());
            responseDTOs.add(dto);
        }
        return ResponseEntity.ok(responseDTOs);
    }

    @PostMapping("/custom-range")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenanceByDateRange(@RequestBody MaintenanceRequestDTO requestDTO) {
        List<Maintenance> rawMaintenance = maintenanceRepository.findByMaintenanceDateBetween(requestDTO.getStartDate(), requestDTO.getEndDate());
        List<MaintenanceResponseDTO> responseDTOs = new ArrayList<>();

        for (Maintenance m : rawMaintenance) {
            MaintenanceResponseDTO dto = new MaintenanceResponseDTO();
            dto.setId(m.getId());
            dto.setEquipmentId(m.getEquipment().getName());
            dto.setMaintenanceDate(m.getMaintenanceDate());
            dto.setStatus(m.getStatus());
            responseDTOs.add(dto);
        }
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/laboratory-equipment-count")
    public ResponseEntity<List<LaboratoryResponseDTO>> getLaboratoryEquipmentCountReport() {
        List<Laboratory> laboratories = laboratoryRepository.findAll();
        List<Laboratory> processedLaboratories = new ArrayList<>();

        // Using a for loop to iterate over real Laboratory entities
        for (Laboratory lab : laboratories) {
            if (lab.getEquipmentList() != null) {
                // Performing operations directly on the real entity object
                processedLaboratories.add(lab);
            }
        }
        return ResponseEntity.ok(processedLaboratories);
    }
}
