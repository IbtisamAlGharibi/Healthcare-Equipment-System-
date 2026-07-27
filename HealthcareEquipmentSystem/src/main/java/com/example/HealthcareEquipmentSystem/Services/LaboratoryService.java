package com.example.HealthcareEquipmentSystem.Services;

import com.example.HealthcareEquipmentSystem.DTO.Requests.LaboratoryRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.LaboratoryResponseDTO;
import com.example.HealthcareEquipmentSystem.Entities.Laboratory;
import com.example.HealthcareEquipmentSystem.Repositories.LaboratoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service

public class LaboratoryService {
    LaboratoryRepository laboratoryRepository;

    @Autowired
    public LaboratoryService(LaboratoryRepository laboratoryRepository) {
        this.laboratoryRepository = laboratoryRepository;
    }

    public LaboratoryResponseDTO addLaboratory(LaboratoryRequestDTO laboratoryRequestDTO) {
        Laboratory newLaboratory = LaboratoryRequestDTO.toEntity(laboratoryRequestDTO);
        newLaboratory.setIsActive(true);
        Laboratory savedLaboratory = laboratoryRepository.save(newLaboratory);
        return LaboratoryResponseDTO.fromEntity(savedLaboratory);
    }
    public LaboratoryResponseDTO updateLaboratory(Integer id, LaboratoryRequestDTO laboratoryRequestDTO) {
        Laboratory updatedLaboratory = laboratoryRepository.findByLaboratoryId(id);
        updatedLaboratory.setName(laboratoryRequestDTO.getName());
        updatedLaboratory.setLocation(laboratoryRequestDTO.getLocation());
        updatedLaboratory.setDescription(laboratoryRequestDTO.getDescription());
        Laboratory savedLaboratory = laboratoryRepository.save(updatedLaboratory);
        return LaboratoryResponseDTO.fromEntity(savedLaboratory);
    }

    public void deleteLaboratory(Integer id) {
        Laboratory deletedLaboratory = laboratoryRepository.findByLaboratoryId(id);
        deletedLaboratory.setIsActive(false);
        laboratoryRepository.save(deletedLaboratory);
    }

    public LaboratoryResponseDTO getLaboratoryById(Integer id) {
        Laboratory laboratory = laboratoryRepository.findByLaboratoryId(id);
        return LaboratoryResponseDTO.fromEntity(laboratory);
    }

    public List<LaboratoryResponseDTO> getAllLaboratories() {
        List<Laboratory> laboratoryList = laboratoryRepository.getAllLaboratories();
        List<LaboratoryResponseDTO> responseDTOList = new ArrayList<>();
        for (Laboratory laboratory : laboratoryList) {
            responseDTOList.add(LaboratoryResponseDTO.fromEntity(laboratory));
        }
        return responseDTOList;
    }

    public LaboratoryResponseDTO getLaboratoryByName(String name) {
        Laboratory laboratory = laboratoryRepository.findByLaboratoryName(name);
        return LaboratoryResponseDTO.fromEntity(laboratory);
    }

    public List<LaboratoryResponseDTO> getLaboratoryByLocation(String location) {
        List<Laboratory> laboratoryList = laboratoryRepository.findByLocation(location);
        List<LaboratoryResponseDTO> responseDTOList = new ArrayList<>();
        for (Laboratory laboratory : laboratoryList) {
            responseDTOList.add(LaboratoryResponseDTO.fromEntity(laboratory));
        }
        return responseDTOList;
    }
}