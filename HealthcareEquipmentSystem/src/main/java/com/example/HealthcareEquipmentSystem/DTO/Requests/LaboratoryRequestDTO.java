package com.example.HealthcareEquipmentSystem.DTO.Requests;
import com.example.HealthcareEquipmentSystem.Entities.Laboratory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratoryRequestDTO {
    private Integer id;

    private String name;

    private String location;

    private String description;

    private Boolean isActive;


    public static Laboratory toEntity(LaboratoryRequestDTO laboratoryRequestDTO){

        Laboratory laboratory = new Laboratory();
        laboratory.setName(laboratoryRequestDTO.getName());
        laboratory.setDescription(laboratoryRequestDTO.getDescription());
        laboratory.setLocation(laboratoryRequestDTO.getLocation());
        laboratory.setIsActive(laboratoryRequestDTO.getIsActive());

        return laboratory;
    }

    }


