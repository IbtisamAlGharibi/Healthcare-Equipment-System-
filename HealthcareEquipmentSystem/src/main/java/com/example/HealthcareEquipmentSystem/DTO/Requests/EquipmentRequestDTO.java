package com.example.HealthcareEquipmentSystem.DTO.Requests;

import com.example.HealthcareEquipmentSystem.Entities.Equipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentRequestDTO {

    private Integer id;

    private String name;

    private String serialNumber;

    private String status;

    private LocalDate purchaseDate;

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






