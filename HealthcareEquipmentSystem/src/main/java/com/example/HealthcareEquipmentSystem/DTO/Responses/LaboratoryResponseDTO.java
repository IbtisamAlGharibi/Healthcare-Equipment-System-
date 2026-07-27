package com.example.HealthcareEquipmentSystem.DTO.Responses;

import com.example.HealthcareEquipmentSystem.Entities.Laboratory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratoryResponseDTO {

    private Integer id;

    private String name;

    private String location;

    private String description;

    private Boolean isActive;



    public static LaboratoryResponseDTO fromEntity( Laboratory laboratoryResponse) {
        LaboratoryResponseDTO dto = new LaboratoryResponseDTO();
        dto.setId(laboratoryResponse.getId());
        dto.setName(laboratoryResponse.getName());
        dto.setLocation(laboratoryResponse.getLocation());
        dto.setDescription(laboratoryResponse.getDescription());
        dto.setIsActive(laboratoryResponse.getIsActive());
        return dto;
    }
}

