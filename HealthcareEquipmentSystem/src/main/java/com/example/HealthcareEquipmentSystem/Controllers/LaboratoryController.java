package com.example.HealthcareEquipmentSystem.Controllers;
import com.example.HealthcareEquipmentSystem.DTO.Requests.LaboratoryRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.LaboratoryResponseDTO;
import com.example.HealthcareEquipmentSystem.Services.LaboratoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/laboratories")
public class LaboratoryController {
    LaboratoryService laboratoryService;
    @Autowired
    public LaboratoryController(LaboratoryService laboratoryService) {
        this.laboratoryService = laboratoryService;
    }
    @PostMapping("/add")
    public LaboratoryResponseDTO addLaboratory(@Valid @RequestBody LaboratoryRequestDTO laboratoryRequestDTO) {
        return laboratoryService.addLaboratory(laboratoryRequestDTO);
    }

    @PutMapping("/update/{id}")
    public LaboratoryResponseDTO updateLaboratory(@PathVariable Integer id, @RequestBody LaboratoryRequestDTO laboratoryRequestDTO) {
        return laboratoryService.updateLaboratory(id, laboratoryRequestDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLaboratory(@PathVariable Integer id) {
        laboratoryService.deleteLaboratory(id);
    }

    @GetMapping("/get/{id}")
    public LaboratoryResponseDTO getLaboratoryById(@PathVariable Integer id) {
        return laboratoryService.getLaboratoryById(id);
    }

    @GetMapping("/all")
    public List<LaboratoryResponseDTO> getAllLaboratories() {
        return laboratoryService.getAllLaboratories();
    }
    @GetMapping("/get-by-name")
    public LaboratoryResponseDTO getLaboratoryByName(@RequestParam String name) {
        return laboratoryService.getLaboratoryByName(name);
    }

    @GetMapping("/get-by-location")
    public List<LaboratoryResponseDTO> getLaboratoryByLocation(@RequestParam String location) {
        return laboratoryService.getLaboratoryByLocation(location);
    }
}