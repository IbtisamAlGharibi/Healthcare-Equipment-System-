package com.example.HealthcareEquipmentSystem.DTO.Responses;

import com.example.HealthcareEquipmentSystem.DTO.Requests.TechnicianRequestDTO;
import com.example.HealthcareEquipmentSystem.Entities.MaintenanceTechnician;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianResponseDTO {
    private Integer id;
    private String name;
    private String phone;
    private String specialization;
    private Boolean isActive;

    public static TechnicianResponseDTO fromEntity(MaintenanceTechnician entity){
        TechnicianResponseDTO dto=new TechnicianResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        dto.setSpecialization(entity.getSpecialization());
        dto.setIsActive(entity.getIsActive());

        return dto;
    }
}
