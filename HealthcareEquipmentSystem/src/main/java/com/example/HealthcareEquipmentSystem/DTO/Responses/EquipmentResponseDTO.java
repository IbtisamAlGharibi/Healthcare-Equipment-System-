package com.example.HealthcareEquipmentSystem.DTO.Responses;

import com.example.HealthcareEquipmentSystem.Entities.Equipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentResponseDTO {

    private Integer id;

    private String name;

    private String serialNumber;

    private String status;

    private LocalDate purchaseDate;

    private String PurchaseName;

    private String laboratoryName;

    private Boolean isActive;



    public static EquipmentResponseDTO fromEntity(Equipment equipment) {

        EquipmentResponseDTO dto = new EquipmentResponseDTO();
        dto.setId(equipment.getId());
        dto.setName(equipment.getName());
        dto.setPurchaseName(String.valueOf(equipment.getPurchaseDate()));
        dto.setStatus(equipment.getStatus());
        dto.setIsActive(equipment.getIsActive());

        return dto;

        }
    }



