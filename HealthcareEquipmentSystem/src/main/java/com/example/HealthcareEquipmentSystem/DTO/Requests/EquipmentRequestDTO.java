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

    private Integer id;
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

    public static Equipment toEntity(EquipmentRequestDTO equipmentRequestDTO){
        Equipment equipment = new Equipment();
        equipment.setName(equipmentRequestDTO.getName());
        equipment.setId(equipmentRequestDTO.getId());
        equipment.setSerialNumber(equipmentRequestDTO.getSerialNumber());
        equipment.setIsActive(equipmentRequestDTO.getIsActive());
        equipment.setPurchaseDate(equipmentRequestDTO.getPurchaseDate());

        return equipment;
    }

    }






