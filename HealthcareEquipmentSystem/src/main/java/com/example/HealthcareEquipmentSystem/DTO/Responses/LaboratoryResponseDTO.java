package com.example.HealthcareEquipmentSystem.DTO.Responses;

import com.example.HealthcareEquipmentSystem.Entities.Equipment;
import com.example.HealthcareEquipmentSystem.Entities.Laboratory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratoryResponseDTO {

    private Integer id;
    private String name;
    private String location;
    private String description;
    private Boolean isActive;
    private List<EquipmentResponseDTO> equipments;

    public static LaboratoryResponseDTO fromEntity(Laboratory laboratory) {
        LaboratoryResponseDTO dto = new LaboratoryResponseDTO();
        dto.setId(laboratory.getId());
        dto.setName(laboratory.getName());
        dto.setLocation(laboratory.getLocation());
        dto.setDescription(laboratory.getDescription());
        dto.setIsActive(laboratory.getIsActive());

        if (laboratory.getEquipments() != null) {
            List<EquipmentResponseDTO> equipmentDTOList = new ArrayList<>();

            for (Equipment equipment : laboratory.getEquipments()) {
                equipmentDTOList.add(EquipmentResponseDTO.fromEntity(equipment));
            }

            dto.setEquipments(equipmentDTOList);
        }

        return dto;
    }
}

