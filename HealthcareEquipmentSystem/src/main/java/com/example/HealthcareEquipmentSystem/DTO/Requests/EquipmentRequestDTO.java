package com.example.HealthcareEquipmentSystem.DTO.Requests;

import com.example.HealthcareEquipmentSystem.Entities.Equipment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Serial number is required")
    private String serialNumber;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Purchase date is required")
    private LocalDate purchaseDate;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @NotNull(message = "Laboratory ID is required")
    private Integer laboratoryId;

    public static Equipment toEntity(EquipmentRequestDTO equipmentRequestDTO){
        Equipment equipment = new Equipment();
        equipment.setName(equipmentRequestDTO.getName());
        equipment.setSerialNumber(equipmentRequestDTO.getSerialNumber());
        equipment.setPurchaseDate(equipmentRequestDTO.getPurchaseDate());

        return equipment;
    }

    }






