package com.example.HealthcareEquipmentSystem.Controllers;

import com.example.HealthcareEquipmentSystem.DTO.Requests.TechnicianRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.TechnicianResponseDTO;
import com.example.HealthcareEquipmentSystem.Services.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technician")
public class TechnicianController {
    private TechnicianService technicianService;
    @Autowired
    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    @PostMapping("/add")
    public ResponseEntity<TechnicianResponseDTO> addTechnician(@RequestBody TechnicianRequestDTO dto) {
        TechnicianResponseDTO response = technicianService.addTechnician(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnicianResponseDTO> updateTechnician(@PathVariable Integer id, @RequestBody TechnicianRequestDTO dto) {
        TechnicianResponseDTO response = technicianService.updateTechnician(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnician(@PathVariable Integer id) {
        technicianService.deleteTechnician(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<TechnicianResponseDTO>> getAllTechnicians() {
        List<TechnicianResponseDTO> response = technicianService.getAllTechnicians();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<TechnicianResponseDTO>> getAllActiveTechnicians() {
        List<TechnicianResponseDTO> response = technicianService.getAllActiveTechnicians();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<TechnicianResponseDTO>> getTechniciansBySpecialization(@PathVariable String specialization) {
        List<TechnicianResponseDTO> response = technicianService.getTechniciansBySpecialization(specialization);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/specialization/{specialization}")
    public ResponseEntity<List<TechnicianResponseDTO>> getActiveTechniciansBySpecialization(@PathVariable String specialization) {
        List<TechnicianResponseDTO> response = technicianService.getActiveTechniciansBySpecialization(specialization);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnicianResponseDTO> getTechnicianById(@PathVariable Integer id) {
        TechnicianResponseDTO response = technicianService.getTechnicianById(id);
        return ResponseEntity.ok(response);
    }
}
