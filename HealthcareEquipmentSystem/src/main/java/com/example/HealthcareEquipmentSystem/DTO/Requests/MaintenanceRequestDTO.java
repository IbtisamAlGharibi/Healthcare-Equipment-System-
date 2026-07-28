package com.example.HealthcareEquipmentSystem.DTO.Requests;

import com.example.HealthcareEquipmentSystem.Entities.Maintenance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequestDTO {
    private LocalDate maintenanceDate;
    @NotBlank(message = "Description Can't Be Empty")
    private String description;
    @Pattern(regexp = "PENDING|...| UNDER_MAINTENANCE |...| COMPLETE |...| AVAILABLE")
    private String status;
    @NotNull(message = "Technician ID is required")
    private Integer technicianId;
    @NotNull(message = "Equipment ID is required")
    private Integer equipmentId;

    public static Maintenance toEntity(MaintenanceRequestDTO dto){
        Maintenance maintenance=new Maintenance();
        maintenance.setMaintenanceDate(dto.getMaintenanceDate());
        maintenance.setDescription(dto.getDescription());
        maintenance.setStatus(dto.getStatus());
        maintenance.setTechnicianId(dto.getTechnicianId());
        maintenance.setEquipmentId(dto.getEquipmentId());

        return maintenance;
    }
}
