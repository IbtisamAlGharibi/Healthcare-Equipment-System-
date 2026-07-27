package com.example.HealthcareEquipmentSystem.DTO.Responses;

import com.example.HealthcareEquipmentSystem.Entities.LaboratoryStaff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratoryStaffResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private Boolean isActive;

    public static LaboratoryStaffResponseDTO fromEntity(LaboratoryStaff laboratoryStaff) {
        LaboratoryStaffResponseDTO dto = new LaboratoryStaffResponseDTO();
        dto.setId(laboratoryStaff.getId());
        dto.setName(laboratoryStaff.getName());
        dto.setEmail(laboratoryStaff.getEmail());
        dto.setPhone(laboratoryStaff.getPhone());
        dto.setDepartment(laboratoryStaff.getDepartment());
        dto.setIsActive(laboratoryStaff.getIsActive());
        return dto;
    }
}
