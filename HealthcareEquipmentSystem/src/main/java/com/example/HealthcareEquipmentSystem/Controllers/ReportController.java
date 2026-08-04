package com.example.HealthcareEquipmentSystem.Controllers;

import com.example.HealthcareEquipmentSystem.DTO.Requests.LaboratoryRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Requests.MaintenanceRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.EquipmentResponseDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.LaboratoryResponseDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.MaintenanceResponseDTO;
import com.example.HealthcareEquipmentSystem.Entities.*;
import com.example.HealthcareEquipmentSystem.Repositories.EquipmentRepository;
import com.example.HealthcareEquipmentSystem.Repositories.LaboratoryRepository;
import com.example.HealthcareEquipmentSystem.Repositories.MaintenanceRepository;
import com.example.HealthcareEquipmentSystem.Services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    private ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService){
        this.reportService=reportService;
    }

    @GetMapping("/equipment/available-count")
    public ResponseEntity<Integer> getAvailableEquipmentCount() {
        Integer count = reportService.availableEquipment();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/equipment/reserved-count")
    public ResponseEntity<Integer> getReservedEquipmentCount() {
        Integer count = reportService.reservedEquipment();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/equipment/maintenance-count")
    public ResponseEntity<Integer> getMaintenanceEquipmentCount() {
        Integer count = reportService.maintenanceEquipment();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/laboratory/equipment")
    public ResponseEntity<List<Equipment>> getEquipmentPerLaboratory() {
        List<Equipment> equipmentList = reportService.equipmentPerLaboratory();
        return ResponseEntity.ok(equipmentList);
    }

    @GetMapping("/laboratory/reservations")
    public ResponseEntity<List<Reservation>> getReservationsPerLaboratory() {
        List<Reservation> laboratories = reportService.reservationsPerLaboratory();
        return ResponseEntity.ok(laboratories);
    }

    @GetMapping("/maintenance/repaired-this-month")
    public ResponseEntity<List<Maintenance>> getEquipmentRepairedThisMonth(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<Maintenance> maintenanceList = reportService.equipmentRepairedThisMonth(startDate, endDate);
        return ResponseEntity.ok(maintenanceList);
    }
}
