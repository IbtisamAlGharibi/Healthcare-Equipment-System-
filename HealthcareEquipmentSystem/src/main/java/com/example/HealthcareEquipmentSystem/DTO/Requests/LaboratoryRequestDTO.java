package com.example.HealthcareEquipmentSystem.DTO.Requests;
import com.example.HealthcareEquipmentSystem.Entities.Laboratory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratoryRequestDTO {

    private Integer id;
   @NotBlank
    private String name;
    @NotBlank
    private String location;
    @NotBlank
    private String description;
    @NotNull
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


