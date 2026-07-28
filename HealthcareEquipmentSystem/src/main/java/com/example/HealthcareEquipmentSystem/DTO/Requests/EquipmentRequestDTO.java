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
    @NotBlank
    private String name;
    @NotBlank
    private String serialNumber;
    @NotBlank
    private String status;
    @NotNull
    private LocalDate purchaseDate;
    @NotNull
    private Boolean isActive;
    @NotNull(message = "Laboratory ID is required")
    private Integer laboratory_id;

    public static Equipment toEntity(EquipmentRequestDTO equipmentRequestDTO){
        Equipment equipment = new Equipment();
        equipment.setName(equipmentRequestDTO.getName());
        equipment.setSerialNumber(equipmentRequestDTO.getSerialNumber());
        equipment.setPurchaseDate(equipmentRequestDTO.getPurchaseDate());

        return equipment;
    }

    }






