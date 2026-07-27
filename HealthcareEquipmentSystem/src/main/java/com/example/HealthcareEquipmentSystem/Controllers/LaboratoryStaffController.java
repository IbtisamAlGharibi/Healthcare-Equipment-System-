package com.example.HealthcareEquipmentSystem.Controllers;

import com.example.HealthcareEquipmentSystem.DTO.Requests.LaboratoryStaffRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.LaboratoryStaffResponseDTO;
import com.example.HealthcareEquipmentSystem.Services.LaboratoryStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/laboratory-staff")
public class LaboratoryStaffController {
    LaboratoryStaffService laboratoryStaffService;
    @Autowired
    public LaboratoryStaffController(LaboratoryStaffService laboratoryStaffService) {
        this.laboratoryStaffService = laboratoryStaffService;
    }

    // Add a new laboratory staff
    @PostMapping
    public ResponseEntity<LaboratoryStaffResponseDTO> addLaboratoryStaff(@RequestBody LaboratoryStaffRequestDTO laboratoryStaffRequestDTO) {
        return ResponseEntity.ok(laboratoryStaffService.addLaboratoryStaff(laboratoryStaffRequestDTO));
    }

    // Update laboratory staff
    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryStaffResponseDTO> updateLaboratoryStaff(@PathVariable Integer id,
            @RequestBody LaboratoryStaffRequestDTO laboratoryStaffRequestDTO) {
        return ResponseEntity.ok(laboratoryStaffService.updateLaboratoryStaff(id, laboratoryStaffRequestDTO));
    }

    // Soft delete laboratory staff
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLaboratoryStaff(@PathVariable Integer id) {
        laboratoryStaffService.deleteLaboratoryStaff(id);
        return ResponseEntity.ok("Laboratory staff deleted successfully.");
    }

    // Get laboratory staff by ID
    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryStaffResponseDTO> getLaboratoryStaffById(@PathVariable Integer id) {
        return ResponseEntity.ok(laboratoryStaffService.getLaboratoryStaffById(id));
    }

    // Get all laboratory staff
    @GetMapping
    public ResponseEntity<List<LaboratoryStaffResponseDTO>> getAllLaboratoryStaff() {
        return ResponseEntity.ok(laboratoryStaffService.getAllLaboratoryStaff());
    }

    // Get laboratory staff by name
    @GetMapping("/name/{name}")
    public ResponseEntity<LaboratoryStaffResponseDTO> getLaboratoryStaffByName(@PathVariable String name) {
        return ResponseEntity.ok(laboratoryStaffService.getLaboratoryStaffByName(name));
    }

    // Get laboratory staff by department
    @GetMapping("/department/{department}")
    public ResponseEntity<List<LaboratoryStaffResponseDTO>> getLaboratoryStaffByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(laboratoryStaffService.getLaboratoryStaffByDepartment(department));
    }
}
