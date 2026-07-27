package com.example.HealthcareEquipmentSystem.Controllers;
import com.example.HealthcareEquipmentSystem.DTO.Requests.EquipmentRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.EquipmentResponseDTO;
import com.example.HealthcareEquipmentSystem.Services.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/laboratories")
public class LaboratoryController {
    EquipmentService equipmentService;

    @Autowired
    public void EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping
    public EquipmentResponseDTO addEquipment(@RequestBody EquipmentRequestDTO equipmentRequestDTO) {
        return equipmentService.addEquipment(equipmentRequestDTO);
    }

    @PutMapping("/{id}")
    public EquipmentResponseDTO updateEquipment(@PathVariable Integer id, @RequestBody EquipmentRequestDTO equipmentRequestDTO) {
        return equipmentService.updateEquipment(id, equipmentRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteEquipment(@PathVariable Integer id) {
        equipmentService.deleteEquipment(id);
    }

    @PutMapping("/{id}/status")
    public EquipmentResponseDTO changeStatus(@PathVariable Integer id, @RequestParam String status) {
        return equipmentService.changeStatus(id, status);
    }

    @GetMapping
    public List<EquipmentResponseDTO> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }

    @GetMapping("/available")
    public List<EquipmentResponseDTO> getAvailableEquipment() {
        return equipmentService.getAvailableEquipment();
    }

    @GetMapping("/{id}")
    public EquipmentResponseDTO getEquipmentById(@PathVariable Integer id) {
        return equipmentService.getEquipmentById(id);
    }
}