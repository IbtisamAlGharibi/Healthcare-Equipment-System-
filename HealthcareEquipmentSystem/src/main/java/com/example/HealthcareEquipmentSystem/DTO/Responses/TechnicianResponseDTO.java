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

    public static TechnicianResponseDTO fromEntity(MaintenanceTechnician technician){
        TechnicianResponseDTO dto=new TechnicianResponseDTO();
        dto.setId(technician.getId());
        dto.setName(technician.getName());
        dto.setPhone(technician.getPhone());
        dto.setSpecialization(technician.getSpecialization());
        dto.setIsActive(technician.getIsActive());

        return dto;
    }
}
