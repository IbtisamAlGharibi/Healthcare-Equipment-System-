package com.example.HealthcareEquipmentSystem.DTO.Responses;

import com.example.HealthcareEquipmentSystem.Entities.Maintenance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceResponseDTO {
    private Integer id;
    private LocalDate maintenanceDate;
    private String description;
    private String status;
    private Integer technicianId;
    private Integer equipmentId;

    public static MaintenanceResponseDTO fromEntity(Maintenance entity){
        MaintenanceResponseDTO dto=new MaintenanceResponseDTO();
        dto.setId(entity.getId());
        dto.setMaintenanceDate(entity.getMaintenanceDate());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setTechnicianId(entity.getTechnicianId());
        dto.setEquipmentId(entity.getEquipmentId());

        return dto;
    }

}
