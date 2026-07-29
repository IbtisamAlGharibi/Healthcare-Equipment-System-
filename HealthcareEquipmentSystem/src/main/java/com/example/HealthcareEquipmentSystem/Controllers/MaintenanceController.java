package com.example.HealthcareEquipmentSystem.Controllers;

import com.example.HealthcareEquipmentSystem.DTO.Requests.MaintenanceRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.MaintenanceResponseDTO;
import com.example.HealthcareEquipmentSystem.Services.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
public class MaintenanceController {
    private MaintenanceService maintenanceService;
    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @PostMapping("/add")
    public ResponseEntity<MaintenanceResponseDTO> createMaintenance(@Valid @RequestBody MaintenanceRequestDTO dto) {
        MaintenanceResponseDTO response = maintenanceService.createMaintenance(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<MaintenanceResponseDTO> completeMaintenance(@PathVariable Integer id) {
        MaintenanceResponseDTO response = maintenanceService.completeMaintenance(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MaintenanceResponseDTO>> getAllMaintenance() {
        List<MaintenanceResponseDTO> response = maintenanceService.getAllMaintenance();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> getMaintenanceById(@PathVariable Integer id) {
        MaintenanceResponseDTO response = maintenanceService.getMaintenanceById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenanceByStatus(@PathVariable String status) {
        List<MaintenanceResponseDTO> response = maintenanceService.getMaintenanceByStatus(status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenanceByTechnicianId(@PathVariable Integer technicianId) {
        List<MaintenanceResponseDTO> response = maintenanceService.getMaintenanceByTechnicianId(technicianId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenanceByEquipmentId(@PathVariable Integer equipmentId) {
        List<MaintenanceResponseDTO> response = maintenanceService.getMaintenanceByEquipmentId(equipmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenanceBetweenDates(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<MaintenanceResponseDTO> response = maintenanceService.getMaintenanceBetweenDates(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMaintenance(@PathVariable Integer id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }
}
